package com.mvproject.tinyiptvkmp.platform.mediaplayer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.mvproject.tinyiptvkmp.core.foundation.common.INT_NO_VALUE
import com.mvproject.tinyiptvkmp.core.foundation.common.INT_VALUE_2
import com.mvproject.tinyiptvkmp.core.foundation.common.INT_VALUE_4
import com.mvproject.tinyiptvkmp.core.foundation.common.INT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.foundation.common.LONG_VALUE_ZERO
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import kotlinx.coroutines.launch
import org.jetbrains.skia.Bitmap
import org.koin.core.component.KoinComponent
import uk.co.caprica.vlcj.factory.MediaPlayerFactory
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery
import uk.co.caprica.vlcj.factory.discovery.provider.AppDirDirectoryProvider
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter
import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent
import uk.co.caprica.vlcj.player.component.MediaPlayerSpecs
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.format.RV32BufferFormat
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.Semaphore
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

private object PlayerViewLogger : KoinComponent {
    val logger by injectLogger("PlayerView")
}

private const val NANOS_IN_MILLIS = 1_000_000L

private fun Long.elapsedMillis(): Long =
    (System.nanoTime() - this) / NANOS_IN_MILLIS

private fun Long.elapsedToMillis(endNanos: Long): Long =
    (endNanos - this) / NANOS_IN_MILLIS

/**
 * Runs VLC native-library discovery once for the desktop player.
 *
 * vlcj 4.12.x changed native discovery providers, so this wrapper keeps discovery
 * logging in one place and records the application-directory candidates without
 * tying the composable render path to discovery work.
 */
private object DesktopVlcDiscovery : KoinComponent {
    private val logger by injectLogger("VlcDiscovery")

    fun discover(): Boolean {
        val appDirProvider = AppDirDirectoryProvider()
        val discovery = NativeDiscovery()
        val isDiscovered = discovery.discover()
        val appDirCandidates = appDirProvider.directories().joinToString()
        val strategyName = discovery.successfulStrategy()?.javaClass?.simpleName

        if (isDiscovered) {
            logger.i {
                "VLC native library discovered at ${discovery.discoveredPath()} using $strategyName; " +
                        "appDir candidates: $appDirCandidates"
            }
        } else {
            logger.e { "VLC native library was not discovered; appDir candidates: $appDirCandidates" }
        }

        return isDiscovered
    }
}

/**
 * Desktop actual media-player surface backed by vlcj callback rendering.
 *
 * The shared player contract and the app-level controls/OSD overlays stay outside
 * this implementation. This composable owns desktop-only setup, forwards state
 * changes to [VideoPlayerStateImpl], and renders decoded frames through Compose.
 */
@Composable
actual fun MediaPlayerView(
    modifier: Modifier,
    state: MediaPlayerState,
    onEvent: (MediaPlayerEvent) -> Unit,
) {
    // todo network Available check

    val isVlcDiscovered = remember { DesktopVlcDiscovery.discover() }
    val latestOnEvent by rememberUpdatedState(onEvent)

    LaunchedEffect(isVlcDiscovered) {
        if (!isVlcDiscovered) {
            latestOnEvent(
                MediaPlayerEvent.PlaybackStateChanged(
                    MediaPlaybackState.Idle(errorCode = null)
                )
            )
        }
    }

    if (!isVlcDiscovered) {
        Canvas(modifier.fillMaxSize()) {}
        return
    }

    val videoPlayerState = remember { VideoPlayerStateImpl() }

    /*    LaunchedEffect(tvPlayerState.isRestartRequired) {
            if (tvPlayerState.isRestartRequired) {
                // todo player restart
                videoPlayerState.restartPlayer()
                onPlaybackAction(UiActions.Restart)
            }
        }*/

    LaunchedEffect(state.volume) {
        videoPlayerState.setVolume(state.volume)
    }

    LaunchedEffect(state.channelKey) {
        if (state.channelKey > INT_NO_VALUE) {
            videoPlayerState.setPlayerChannel(
                channelUrl = state.url
            )
        }
    }

    LaunchedEffect(state.isPlaying) {
        videoPlayerState.setPlayingState(state.isPlaying)
    }

    VideoPlayerDirect(
        modifier = modifier.fillMaxSize(),
        state = videoPlayerState,
        onEvent = onEvent
    )
}

/**
 * Owns the vlcj component and serializes player commands off the Compose thread.
 *
 * LibVLC callbacks may arrive on native callback threads, so direct player control
 * calls are routed through a single executor to avoid re-entering LibVLC from event
 * callbacks and to keep stop/play/volume operations ordered.
 */
internal class VideoPlayerStateImpl : PlatformPlayerState, KoinComponent {
    private val logger by injectLogger()
    private val commandExecutor: ExecutorService = Executors.newSingleThreadExecutor { runnable ->
        Thread(runnable, "TinyIPTV-VLC-Player").apply {
            isDaemon = true
        }
    }
    private val isReleased = AtomicBoolean(false)

    internal val internalState = RenderState()

    val mediaPlayer: MediaPlayer
        get() = internalState.mediaPlayerComponent.mediaPlayer()

    override fun setVolume(value: Float) {
        val volumeValue = (value * 100).toInt().coerceIn(INT_VALUE_ZERO, MAX_VLC_VOLUME)
        submitCommand("setVolume") {
            mediaPlayer.audio().setVolume(volumeValue)
        }
    }

    override fun setPlayingState(value: Boolean) {
        submitCommand("setPlayingState") {
            val isPlaying = mediaPlayer.status().isPlaying
            if (value == isPlaying) {
                return@submitCommand
            }

            if (value) {
                mediaPlayer.controls().play()
            } else {
                mediaPlayer.controls().pause()
            }
        }
    }

    override fun play() {
        submitCommand("play") {
            mediaPlayer.controls().play()
        }
    }

    override fun pause() {
        submitCommand("pause") {
            mediaPlayer.controls().pause()
        }
    }

    override fun restartPlayer() {
        /*        if (this.player.playbackState == Player.STATE_IDLE) {
                    this.player.apply {
                        prepare()
                        playWhenReady = true
                    }
                }*/
    }

    override fun setPlayerChannel(channelUrl: String) {
        submitCommand("setPlayerChannel") {
            mediaPlayer.controls().stop()
            mediaPlayer.media().play(channelUrl)
            mediaPlayer.subpictures().setTrack(INT_NO_VALUE)
        }
    }

    fun logAudioTracks() {
        submitCommand("logAudioTracks") {
            mediaPlayer.media().info().audioTracks().forEach { info ->
                logger.i { "audioTrack info: $info" }
            }
        }
    }

    fun release() {
        if (!isReleased.compareAndSet(false, true)) {
            return
        }

        try {
            commandExecutor.execute {
                runCatching {
                    mediaPlayer.controls().stop()
                    internalState.release()
                }.onFailure { ex ->
                    logger.e(ex) { "release failed ${ex.localizedMessage}" }
                }
            }
        } catch (ex: RejectedExecutionException) {
            logger.e(ex) { "release command rejected ${ex.localizedMessage}" }
            runCatching {
                internalState.release()
            }.onFailure { releaseEx ->
                logger.e(releaseEx) { "fallback release failed ${releaseEx.localizedMessage}" }
            }
        } finally {
            commandExecutor.shutdown()
        }
    }

    private fun submitCommand(actionName: String, command: () -> Unit) {
        if (isReleased.get()) {
            return
        }

        try {
            commandExecutor.execute {
                if (isReleased.get()) {
                    return@execute
                }

                runCatching(command).onFailure { ex ->
                    logger.e(ex) { "$actionName failed ${ex.localizedMessage}" }
                }
            }
        } catch (ex: RejectedExecutionException) {
            logger.e(ex) { "$actionName command rejected ${ex.localizedMessage}" }
        }
    }

    init {
        logger.d { "init VideoPlayerStateImpl" }
    }

    private companion object {
        const val MAX_VLC_VOLUME = 100
    }
}

/**
 * Binds vlcj event callbacks to Compose and draws the current callback frame.
 *
 * Event callbacks only dispatch lightweight app events or enqueue diagnostics on
 * [VideoPlayerStateImpl]. The actual video image is drawn by Compose Canvas so the
 * app can keep its custom controls and OSD overlays above this surface.
 */
@Composable
internal fun VideoPlayerDirect(
    modifier: Modifier = Modifier,
    state: VideoPlayerStateImpl = remember { VideoPlayerStateImpl() },
    onEvent: (MediaPlayerEvent) -> Unit
) {
    val eventScope = rememberCoroutineScope()
    val latestOnEvent by rememberUpdatedState(onEvent)

    DisposableEffect(state) {
        fun dispatchEvent(event: MediaPlayerEvent) {
            eventScope.launch {
                latestOnEvent(event)
            }
        }

        val eventListener = object : MediaPlayerEventAdapter() {
            override fun error(mediaPlayer: MediaPlayer) {
                PlayerViewLogger.logger.e { "mediaPlayer error" }
                dispatchEvent(
                    MediaPlayerEvent.PlaybackStateChanged(
                        MediaPlaybackState.Idle(errorCode = null)
                    )
                )
            }

            override fun mediaPlayerReady(mediaPlayer: MediaPlayer) {
                state.logAudioTracks()
                dispatchEvent(
                    MediaPlayerEvent.PlaybackStateChanged(MediaPlaybackState.Ready)
                )
            }

            override fun buffering(mediaPlayer: MediaPlayer, newCache: Float) {
                // Keep buffering notifications quiet here; the shared API is preserved.
            }

            override fun playing(mediaPlayer: MediaPlayer) {
                dispatchEvent(MediaPlayerEvent.PlayingChanged(isPlaying = true))
            }

            override fun paused(mediaPlayer: MediaPlayer) {
                dispatchEvent(MediaPlayerEvent.PlayingChanged(isPlaying = false))
            }

            override fun stopped(mediaPlayer: MediaPlayer) {
                dispatchEvent(
                    MediaPlayerEvent.PlaybackStateChanged(MediaPlaybackState.Ended)
                )
            }
        }

        state.mediaPlayer.events().addMediaPlayerEventListener(eventListener)

        onDispose {
            state.mediaPlayer.events().removeMediaPlayerEventListener(eventListener)
            state.release()
        }
    }

    var frameTime: Long by remember { mutableStateOf(LONG_VALUE_ZERO) }

    LaunchedEffect(state) {
        while (true) {
            withFrameMillis { time ->
                frameTime = time
            }
        }
    }
    return Canvas(modifier) {
        state.internalState.updateComposeImage(frameTime)?.let { image ->
            val offsetY = ((size.height - image.height) / INT_VALUE_4)
                .toInt()
                .coerceAtLeast(INT_VALUE_ZERO)

            val imageHeight = size.height.toInt() - offsetY * INT_VALUE_2
            val imageWidth = size.width.toInt()

            drawImage(
                image = image,
                dstSize = IntSize(imageWidth, imageHeight),
                dstOffset = IntOffset(INT_VALUE_ZERO, offsetY),
                filterQuality = FilterQuality.Medium
            )
        }
    }
}

/**
 * Coordinates LibVLC callback buffers with Compose bitmap drawing.
 *
 * VLC can renegotiate callback buffer size after playback starts. New render
 * resources are kept pending until a decoded frame has been copied successfully,
 * so the previous visible frame remains on screen instead of flashing an empty
 * bitmap. Timing counters here identify whether a stall happens before the first
 * LibVLC display callback or during Compose-side copying.
 */
internal class RenderState : KoinComponent {
    private val logger by injectLogger()
    private val frameLock = Semaphore(1)
    private val dirtyFrame = AtomicLong(LONG_VALUE_ZERO)
    private val formatGeneration = AtomicLong(LONG_VALUE_ZERO)
    private val frameLockMissCount = AtomicLong(LONG_VALUE_ZERO)
    private val mediaPlayerFactory = MediaPlayerFactory(*LIBVLC_ARGS)

    @Volatile
    private var visibleFrame: RenderFrame? = null

    @Volatile
    private var pendingFrame: RenderFrame? = null
    private var renderedFrame: Long = LONG_VALUE_ZERO

    init {
        logger.d { "init RenderState" }
    }

    fun updateComposeImage(frameTime: Long): ImageBitmap? {
        logPendingFrameStallIfNeeded()

        val nextFrame = dirtyFrame.get()
        val currentVisibleFrame = visibleFrame

        if (nextFrame == renderedFrame) {
            return currentVisibleFrame?.image
        }

        if (!frameLock.tryAcquire()) {
            frameLockMissCount.incrementAndGet()
            return currentVisibleFrame?.image
        }

        try {
            val lockedFrame = dirtyFrame.get()
            val frame = pendingFrame ?: visibleFrame
            if (lockedFrame != renderedFrame) {
                val byteBuffer = frame?.nativeBuffer ?: return visibleFrame?.image
                val copyStartedAt = System.nanoTime()
                byteBuffer.get(frame.buffer)
                byteBuffer.rewind()
                frame.bitmap.installPixels(frame.buffer)
                val copyTimeMillis = copyStartedAt.elapsedMillis()
                renderedFrame = lockedFrame

                if (copyTimeMillis > SLOW_COPY_WARNING_MILLIS) {
                    logger.w {
                        "slow frame copy generation=${frame.generation} copy=${copyTimeMillis}ms " +
                                "size=${frame.width}x${frame.height}"
                    }
                }

                if (frame === pendingFrame) {
                    pendingFrame = null
                    visibleFrame = frame
                    logger.i {
                        "format ${frame.generation} visible after ${frame.elapsedSinceCreatedMillis()}ms; " +
                                "formatToSize=${frame.elapsedFromCreatedToFormatSizeMillis()}ms; " +
                                "formatSizeToFirstDisplay=${frame.elapsedFromFormatSizeToFirstDisplayMillis()}ms; " +
                                "firstDisplayToVisible=${frame.elapsedFromFirstDisplayToNowMillis()}ms; " +
                                "first copy ${copyTimeMillis}ms; " +
                                "pendingDisplays=${frame.displayCount()}; " +
                                "frameLockMisses=${frameLockMissCount.get()}"
                    }
                }
            }
        } catch (ex: Exception) {
            logger.e(ex) { "updateComposeImage exception ${ex.localizedMessage}" }
        } finally {
            frameLock.release()
        }

        return visibleFrame?.image
    }

    private val bufferFormatCallback = object : BufferFormatCallback {
        override fun getBufferFormat(sourceWidth: Int, sourceHeight: Int): BufferFormat {
            frameLock.acquireUninterruptibly()
            try {
                val bitmap = Bitmap().also {
                    it.allocN32Pixels(sourceWidth, sourceHeight, true)
                }

                pendingFrame = RenderFrame(
                    generation = formatGeneration.incrementAndGet(),
                    width = sourceWidth,
                    height = sourceHeight,
                    buffer = ByteArray(sourceWidth * sourceHeight * INT_VALUE_4),
                    bitmap = bitmap,
                    image = bitmap.asComposeImageBitmap()
                )
                logger.i {
                    "getBufferFormat generation=${pendingFrame?.generation} " +
                            "pending=${sourceWidth}x$sourceHeight visible=${visibleFrame?.sizeLabel() ?: "none"}"
                }
            } finally {
                frameLock.release()
            }

            return RV32BufferFormat(sourceWidth, sourceHeight)
        }

        override fun newFormatSize(
            bufferWidth: Int,
            bufferHeight: Int,
            displayWidth: Int,
            displayHeight: Int
        ) {
            frameLock.acquireUninterruptibly()
            try {
                val frame = pendingFrame
                frame?.displayWidth = displayWidth
                frame?.displayHeight = displayHeight
                frame?.markFormatSize()
                val visibleSize = visibleFrame?.let { "${it.width}x${it.height}" } ?: "none"
                logger.i {
                    "newFormatSize generation=${frame?.generation} visible=$visibleSize " +
                            "pending=${bufferWidth}x$bufferHeight display=${displayWidth}x$displayHeight " +
                            "getBufferFormatToNewFormatSize=${frame?.elapsedFromCreatedToFormatSizeMillis()}ms"
                }
            } finally {
                frameLock.release()
            }
        }

        override fun allocatedBuffers(buffers: Array<out ByteBuffer>) {
            frameLock.acquireUninterruptibly()
            try {
                pendingFrame?.nativeBuffer = buffers.firstOrNull()
            } finally {
                frameLock.release()
            }
        }
    }
    private val renderCallback = object : RenderCallback {
        override fun lock(mediaPlayer: MediaPlayer) {
            frameLock.acquireUninterruptibly()
        }

        override fun display(
            mediaPlayer: MediaPlayer,
            nativeBuffers: Array<out ByteBuffer>,
            bufferFormat: BufferFormat,
            displayWidth: Int,
            displayHeight: Int
        ) {
            val frame = pendingFrame ?: visibleFrame
            if (frame != null) {
                frame.nativeBuffer = nativeBuffers.firstOrNull()
                frame.markDisplay()
                dirtyFrame.incrementAndGet()
            }
        }

        override fun unlock(mediaPlayer: MediaPlayer) {
            frameLock.release()
        }
    }
    val mediaPlayerComponent: CallbackMediaPlayerComponent = MediaPlayerSpecs
        .callbackMediaPlayerSpec()
        .withFactory(mediaPlayerFactory)
        .withLockedBuffers()
        .withRenderCallback(renderCallback)
        .withBufferFormatCallback(bufferFormatCallback)
        .callbackMediaPlayer()

    fun release() {
        try {
            mediaPlayerComponent.release()
        } finally {
            mediaPlayerFactory.release()
        }
    }

    private fun logPendingFrameStallIfNeeded() {
        val frame = pendingFrame ?: return
        if (frame.shouldWarnPendingStall(PENDING_FRAME_WARNING_MILLIS)) {
            logger.w {
                "pending format ${frame.generation} not visible after ${frame.elapsedSinceCreatedMillis()}ms; " +
                        "formatToSize=${frame.elapsedFromCreatedToFormatSizeMillis()}ms; " +
                        "formatSizeToFirstDisplay=${frame.elapsedFromFormatSizeToFirstDisplayMillis()}ms; " +
                        "pendingDisplays=${frame.displayCount()}; " +
                        "frameLockMisses=${frameLockMissCount.get()}; " +
                        "size=${frame.sizeLabel()} display=${frame.displayWidth}x${frame.displayHeight}"
            }
        }
    }

    /**
     * Holds one VLC callback buffer generation and its Compose image wrapper.
     *
     * Instances start as pending resources and become the visible frame only after
     * the first successful copy from the native buffer into the Skia bitmap.
     */
    private class RenderFrame(
        val generation: Long,
        val width: Int,
        val height: Int,
        val buffer: ByteArray,
        val bitmap: Bitmap,
        val image: ImageBitmap,
        val createdAtMillis: Long = System.currentTimeMillis(),
    ) {
        private val displayCount = AtomicLong(LONG_VALUE_ZERO)
        private val firstDisplayAtNanos = AtomicLong(LONG_VALUE_ZERO)
        private val pendingStallWarned = AtomicBoolean(false)

        @Volatile
        var nativeBuffer: ByteBuffer? = null

        @Volatile
        var displayWidth: Int = width

        @Volatile
        var displayHeight: Int = height

        @Volatile
        var formatSizeAtNanos: Long = LONG_VALUE_ZERO

        fun elapsedSinceCreatedMillis(): Long = System.currentTimeMillis() - createdAtMillis

        fun markFormatSize() {
            formatSizeAtNanos = System.nanoTime()
        }

        fun markDisplay() {
            val now = System.nanoTime()
            displayCount.incrementAndGet()
            firstDisplayAtNanos.compareAndSet(LONG_VALUE_ZERO, now)
        }

        fun displayCount(): Long = displayCount.get()

        fun elapsedFromCreatedToFormatSizeMillis(): Long? =
            formatSizeAtNanos.takeIf { it > LONG_VALUE_ZERO }
                ?.let { createdAtNanos.elapsedToMillis(it) }

        fun elapsedFromFormatSizeToFirstDisplayMillis(): Long? {
            val formatSize = formatSizeAtNanos
            val firstDisplay = firstDisplayAtNanos.get()
            return if (formatSize > LONG_VALUE_ZERO && firstDisplay > LONG_VALUE_ZERO) {
                formatSize.elapsedToMillis(firstDisplay)
            } else {
                null
            }
        }

        fun elapsedFromFirstDisplayToNowMillis(): Long? =
            firstDisplayAtNanos.get()
                .takeIf { it > LONG_VALUE_ZERO }
                ?.elapsedMillis()

        fun shouldWarnPendingStall(thresholdMillis: Long): Boolean =
            elapsedSinceCreatedMillis() > thresholdMillis &&
                    pendingStallWarned.compareAndSet(false, true)

        fun sizeLabel(): String = "${width}x$height"

        private val createdAtNanos: Long = System.nanoTime()
    }

    private companion object {
        const val PENDING_FRAME_WARNING_MILLIS = 500L
        const val SLOW_COPY_WARNING_MILLIS = 16L
        val LIBVLC_ARGS = arrayOf(
            "--network-caching=1000",
            "--live-caching=1000",
            "--drop-late-frames",
            "--skip-frames",
            "--no-video-title-show"
        )
    }
}
