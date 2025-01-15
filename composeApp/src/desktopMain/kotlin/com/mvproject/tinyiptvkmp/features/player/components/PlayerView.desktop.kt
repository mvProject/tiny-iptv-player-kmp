package com.mvproject.tinyiptvkmp.features.player.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import co.touchlab.kermit.Logger
import com.mvproject.tinyiptvkmp.core.common.AppConstants.FLOAT_VALUE_1
import com.mvproject.tinyiptvkmp.core.common.AppConstants.INT_NO_VALUE
import com.mvproject.tinyiptvkmp.core.common.AppConstants.INT_VALUE_2
import com.mvproject.tinyiptvkmp.core.common.AppConstants.INT_VALUE_4
import com.mvproject.tinyiptvkmp.core.common.AppConstants.INT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.common.AppConstants.LONG_VALUE_ZERO
import com.mvproject.tinyiptvkmp.features.player.PlayerUiAction
import com.mvproject.tinyiptvkmp.features.player.PlayerUiState
import org.jetbrains.skia.Bitmap
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery
import uk.co.caprica.vlcj.media.MediaRef
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter
import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.format.RV32BufferFormat
import java.nio.ByteBuffer

@Composable
actual fun PlayerView(
    modifier: Modifier,
    uiState: PlayerUiState,
    onAction: (PlayerUiAction) -> Unit,
) {
    // todo network Available check

    val videoPlayerState = remember { VideoPlayerStateImpl() }

    /*    LaunchedEffect(tvPlayerState.isRestartRequired) {
            if (tvPlayerState.isRestartRequired) {
                // todo player restart
                videoPlayerState.restartPlayer()
                onPlaybackAction(UiActions.Restart)
            }
        }*/

    LaunchedEffect(uiState.isFullscreen) {
        // todo handle fullscreen state
    }

    LaunchedEffect(uiState.currentVolume) {
        videoPlayerState.setVolume(uiState.currentVolume)
    }

    LaunchedEffect(uiState.channelIndex) {
        if (uiState.channelIndex > INT_NO_VALUE) {
            videoPlayerState.setPlayerChannel(
                channelUrl = uiState.currentChannel.channelUrl
            )
        }
    }

    LaunchedEffect(uiState.isPlaying) {
        videoPlayerState.setPlayingState(uiState.isPlaying)
    }

    VideoPlayerDirect(
        modifier = modifier.fillMaxSize(),
        state = videoPlayerState,
        url = uiState.currentChannel.channelUrl,
        onPlaybackAction = onAction
    )
}

class VideoPlayerStateImpl : PlayerState {
    internal val internalState = RenderState()
    val mediaPlayer: MediaPlayer
        get() = internalState.mediaPlayerComponent.mediaPlayer()
    val aspectRatio: Float
        get() = internalState.aspectRatio

    override fun setVolume(value: Float) {
        val volumeValue = (value * 100).toInt()
        mediaPlayer.audio().setVolume(volumeValue)
    }

    override fun setPlayingState(value: Boolean) {
        if (value) {
            play()
        } else {
            pause()
        }
    }

    override fun play() {
        mediaPlayer.controls().play()
    }

    override fun pause() {
        mediaPlayer.controls().pause()
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
        mediaPlayer.media().play(channelUrl)
    }

    init {
        Logger.w("init VideoPlayerStateImpl")
    }
}

@Composable
fun VideoPlayerDirect(
    modifier: Modifier = Modifier,
    state: VideoPlayerStateImpl = remember { VideoPlayerStateImpl() },
    url: String,
    onPlaybackAction: (PlayerUiAction) -> Unit
) {
    NativeDiscovery().discover()

    DisposableEffect(state) {
        val eventListener = object : MediaPlayerEventAdapter() {
            override fun error(mediaPlayer: MediaPlayer) {
                Logger.e("testing mediaPlayer error")
            }

            override fun mediaPlayerReady(mediaPlayer: MediaPlayer) {
                mediaPlayer.media().info().audioTracks().forEach { info ->
                    Logger.i("testing audioTrack info: $info")
                }

                onPlaybackAction(
                    PlayerUiAction.OnPlaybackStateChanged(PlayerUiState.PlayerPlaybackState.PlaybackReady)
                )
            }

            override fun mediaChanged(mediaPlayer: MediaPlayer, media: MediaRef?) {
                //onAction(
                //    UiActions.OnMediaItemTransition(
                //        mediaTitle = "",
                //        index = 1
                //    )
                //)
            }

            override fun buffering(mediaPlayer: MediaPlayer, newCache: Float) {
                val isPlaying = mediaPlayer.status().isPlaying
                if (!isPlaying) {
                    // onPlaybackStateAction(
                    //     PlaybackStateActions.OnPlaybackStateChanged(VideoPlaybackState.VideoPlaybackBuffering)
                    // )
                }
            }

            override fun playing(mediaPlayer: MediaPlayer) {
                mediaPlayer.status().isPlaying.let { isPlaying ->
                    onPlaybackAction(
                        PlayerUiAction.OnIsPlayingChanged(isPlaying)
                    )
                }
            }

            override fun paused(mediaPlayer: MediaPlayer) {
                mediaPlayer.status().isPlaying.let { isPlaying ->
                    onPlaybackAction(
                        PlayerUiAction.OnIsPlayingChanged(isPlaying)
                    )
                }
            }

            override fun stopped(mediaPlayer: MediaPlayer) {
                onPlaybackAction(
                    PlayerUiAction.OnPlaybackStateChanged(PlayerUiState.PlayerPlaybackState.PlaybackEnded)
                )
            }
        }

        state.mediaPlayer.events().addMediaPlayerEventListener(eventListener)

        onDispose {
            state.mediaPlayer.events().removeMediaPlayerEventListener(eventListener)
            state.mediaPlayer.release()
        }
    }

    var frameTime: Long by remember { mutableStateOf(LONG_VALUE_ZERO) }

    LaunchedEffect(url) {
        Logger.w("testing LaunchedEffect started")
        state.mediaPlayer.media()?.start(url)
        state.mediaPlayer.subpictures().setTrack(INT_NO_VALUE)
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

internal class RenderState {
    var aspectRatio: Float by mutableStateOf(FLOAT_VALUE_1)
        private set

    var currentBuffer: ByteBuffer? = null

    private var buffer: ByteArray = ByteArray(INT_VALUE_ZERO)
    private var bufferBitmap: Bitmap = Bitmap()
    private var composeImage: ImageBitmap? = null

    init {
        Logger.w("init RenderState")
    }

    fun updateComposeImage(frameTime: Long): ImageBitmap? {
        try {
            currentBuffer?.let { byteBuffer ->
                byteBuffer.get(buffer)
                byteBuffer.rewind()
                bufferBitmap.installPixels(buffer)
                return composeImage
            }
        } catch (ex: Exception) {
            Logger.e("updateComposeImage exception ${ex.localizedMessage}")
        }

        return null
    }

    private val bufferFormatCallback = object : BufferFormatCallback {
        override fun getBufferFormat(sourceWidth: Int, sourceHeight: Int): BufferFormat {

            bufferBitmap = Bitmap().also {
                it.allocN32Pixels(sourceWidth, sourceHeight, true)
            }

            composeImage = bufferBitmap.asComposeImageBitmap()

            buffer = ByteArray(sourceWidth * sourceHeight * INT_VALUE_4)

            with(bufferBitmap) {
                aspectRatio = if (width <= INT_VALUE_ZERO || height <= INT_VALUE_ZERO)
                    FLOAT_VALUE_1
                else
                    width.toFloat() / height.toFloat()
            }

            return RV32BufferFormat(sourceWidth, sourceHeight)
        }

        override fun allocatedBuffers(buffers: Array<out ByteBuffer>) {
            currentBuffer = buffers.firstOrNull()
        }
    }
    private val renderCallback = RenderCallback { _, _, _ ->
        //currentBuffer = nativeBuffers.first()
    }
    val mediaPlayerComponent = CallbackMediaPlayerComponent(
        null,
        null,
        null,
        true,
        null,
        renderCallback,
        bufferFormatCallback,
        null
    )
}


/*
@Composable
actual fun PlayerView(
    modifier: Modifier,
    uiState: PlayerUiState,
    onAction: (PlayerUiAction) -> Unit,
) {
    // todo network Available check

    val playerState = rememberPlayerStateSwing(
        onPlaybackStateAction = onAction
    )
*/
/*

    LaunchedEffect(uiState.isRestartRequired) {
        if (uiState.isRestartRequired) {
            // todo player restart
            playerState.restartPlayer()
            onPlaybackAction(PlaybackActions.OnRestarted)
        }
    }
*//*


    LaunchedEffect(uiState.isFullscreen) {
        // todo handle fullscreen state
    }

    LaunchedEffect(uiState.currentVolume) {
        playerState.setVolume(uiState.currentVolume)
    }

    LaunchedEffect(uiState.channelIndex) {
        if (uiState.channelIndex > INT_NO_VALUE) {
            playerState.setPlayerChannel(
                channelUrl = uiState.currentChannel.channelUrl,
            )
            //playerState.restartPlayer()
        }
    }

    LaunchedEffect(uiState.isPlaying) {
        playerState.setPlayingState(uiState.isPlaying)
        w {
            "testing isPlaying:${uiState.isPlaying}"
        }
    }

    //NativeLibrary.addSearchPath(
    //    RuntimeUtil.getLibVlcLibraryName(),
    //    "C:/Program Files/VideoLAN/VLC"
    //)
    //Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc::class.java)

    NativeDiscovery().discover()

    SwingPanel(
        factory = { playerState.playerComponent },
        background = Color.Transparent,
        modifier = modifier.fillMaxSize().background(Color.Yellow),
    )

    DisposableEffect(playerState) {
        onDispose {
            playerState.closePlayer()
        }
    }
}

@Composable
internal fun rememberPlayerStateSwing(
    onPlaybackStateAction: (PlayerUiAction) -> Unit = {}
) = remember {
    PlayerStateSwingImpl(
        playerComponent = CallbackMediaPlayerComponent(),
        onPlaybackStateAction = onPlaybackStateAction
    ).also { playerState ->
        playerState.playerComponent.apply {
            mediaPlayer().events().addMediaPlayerEventListener(playerState)
        }
    }
}

class PlayerStateSwingImpl(
    val playerComponent: CallbackMediaPlayerComponent,
    private val onPlaybackStateAction: (PlayerUiAction) -> Unit = {}
) : PlayerState, MediaPlayerEventAdapter() {

    override fun setVolume(value: Float) {
        val volumeValue = (value * 100).toInt()
        playerComponent.mediaPlayer().audio().setVolume(volumeValue)
    }

    override fun setPlayingState(value: Boolean) {
        if (value) {
            play()
        } else {
            pause()
        }
    }

    override fun play() {
        playerComponent.mediaPlayer().controls().play()
    }

    override fun pause() {
        playerComponent.mediaPlayer().controls().pause()
    }

    override fun restartPlayer() {
        */
/*        if (this.player.playbackState == Player.STATE_IDLE) {
                    this.player.apply {
                        prepare()
                        playWhenReady = true
                    }
                }*//*

    }

    override fun setPlayerChannel(channelUrl: String) {
        playerComponent.mediaPlayer().media().play(channelUrl)
    }

    fun closePlayer(
    ) {
        playerComponent.mediaPlayer().controls().stop()
        playerComponent.mediaPlayer().events().removeMediaPlayerEventListener(this)
        playerComponent.mediaPlayer()::release
    }

    override fun error(mediaPlayer: MediaPlayer?) {}

    override fun mediaPlayerReady(mediaPlayer: MediaPlayer?) {
       // onPlaybackStateAction(
       //     PlayerUiAction.OnPlaybackStateChanged(VideoPlaybackState.VideoPlaybackReady)
       // )
    }

*/
/*    override fun mediaChanged(mediaPlayer: MediaPlayer?, media: MediaRef?) {
        onPlaybackStateAction(
            PlayerUiAction.OnMediaItemTransition(
                mediaTitle = "",
                index = 1
            )
        )
    }*//*


    override fun buffering(mediaPlayer: MediaPlayer?, newCache: Float) {
        val isPlaying = mediaPlayer?.status()?.isPlaying
        if (isPlaying == false) {
            println("buffering isPlaying:$isPlaying")
            // onPlaybackStateAction(
            //     PlaybackStateActions.OnPlaybackStateChanged(VideoPlaybackState.VideoPlaybackBuffering)
            // )
        }
    }

    override fun playing(mediaPlayer: MediaPlayer?) {
        mediaPlayer?.status()?.isPlaying?.let { isPlaying ->
            onPlaybackStateAction(
                PlayerUiAction.OnIsPlayingChanged(isPlaying)
            )
        }
    }

    override fun paused(mediaPlayer: MediaPlayer?) {
        mediaPlayer?.status()?.isPlaying?.let { isPlaying ->
            onPlaybackStateAction(
                PlayerUiAction.OnIsPlayingChanged(isPlaying)
            )
        }
    }

    override fun stopped(mediaPlayer: MediaPlayer?) {
        println("stopped")
       // onPlaybackStateAction(
       //     PlayerUiAction.OnPlaybackStateChanged(VideoPlaybackState.VideoPlaybackEnded)
       // )
    }
}*/
