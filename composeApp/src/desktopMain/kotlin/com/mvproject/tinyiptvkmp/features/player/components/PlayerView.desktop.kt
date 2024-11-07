package com.mvproject.tinyiptvkmp.features.player.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
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
import com.mvproject.tinyiptvkmp.core.common.AppConstants
import com.mvproject.tinyiptvkmp.core.common.AppConstants.FLOAT_VALUE_1
import com.mvproject.tinyiptvkmp.core.common.AppConstants.INT_NO_VALUE
import com.mvproject.tinyiptvkmp.core.common.AppConstants.INT_VALUE_2
import com.mvproject.tinyiptvkmp.core.common.AppConstants.INT_VALUE_4
import com.mvproject.tinyiptvkmp.core.common.AppConstants.INT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.common.AppConstants.LONG_VALUE_ZERO
import com.mvproject.tinyiptvkmp.features.player.action.PlaybackActions
import com.mvproject.tinyiptvkmp.features.player.action.PlaybackStateActions
import com.mvproject.tinyiptvkmp.features.player.state.PlaybackState
import com.mvproject.tinyiptvkmp.features.player.state.TvPlayerState
import com.mvproject.tinyiptvkmp.utils.KLog
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
    tvPlayerState: TvPlayerState,
    onPlaybackAction: (PlaybackActions) -> Unit,
    onPlaybackStateAction: (PlaybackStateActions) -> Unit,
) {
    // todo network Available check

    val videoPlayerState = remember { VideoPlayerStateImpl() }

    LaunchedEffect(tvPlayerState.isRestartRequired) {
        if (tvPlayerState.isRestartRequired) {
            // todo player restart
            videoPlayerState.restartPlayer()
            onPlaybackAction(PlaybackActions.OnRestarted)
        }
    }

    LaunchedEffect(tvPlayerState.isFullscreen) {
        // todo handle fullscreen state
    }

    LaunchedEffect(tvPlayerState.currentVolume) {
        videoPlayerState.setVolume(tvPlayerState.currentVolume)
    }

    LaunchedEffect(tvPlayerState.mediaPosition) {
        if (tvPlayerState.mediaPosition > INT_NO_VALUE) {
            videoPlayerState.setPlayerChannel(
                channelName = tvPlayerState.currentChannel.channelName,
                channelUrl = tvPlayerState.currentChannel.channelUrl
            )
        }
    }

    LaunchedEffect(tvPlayerState.isPlaying) {
        videoPlayerState.setPlayingState(tvPlayerState.isPlaying)
    }

    VideoPlayerDirect(
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(videoPlayerState.aspectRatio),
        state = videoPlayerState,
        url = tvPlayerState.currentChannel.channelUrl,
        onPlaybackStateAction = onPlaybackStateAction
    )


}

interface VideoPlayerState {
    val aspectRatio: Float
    fun play()
    fun pause()
    fun setVolume(value: Float)
    fun setPlayingState(value: Boolean)
    fun restartPlayer()
    fun setPlayerChannel(
        channelName: String,
        channelUrl: String
    )
}

class VideoPlayerStateImpl : VideoPlayerState {
    internal val internalState = RenderState()
    val mediaPlayer: MediaPlayer
        get() = internalState.mediaPlayerComponent.mediaPlayer()
    override val aspectRatio: Float
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

    override fun setPlayerChannel(
        channelName: String,
        channelUrl: String
    ) {
        mediaPlayer.media().play(channelUrl)
    }

    init {
        KLog.w("init VideoPlayerStateImpl")
    }
}

@Composable
fun VideoPlayerDirect(
    modifier: Modifier = Modifier,
    state: VideoPlayerStateImpl = remember { VideoPlayerStateImpl() },
    url: String,
    onPlaybackStateAction: (PlaybackStateActions) -> Unit = {}
) {
    NativeDiscovery().discover()

    DisposableEffect(state) {
        val eventListener = object : MediaPlayerEventAdapter() {
            override fun error(mediaPlayer: MediaPlayer) {
                KLog.e("testing mediaPlayer error")
            }

            override fun mediaPlayerReady(mediaPlayer: MediaPlayer) {
                mediaPlayer.media().info().audioTracks().forEach { info ->
                    KLog.i("testing audioTrack info: $info")
                }

                onPlaybackStateAction(
                    PlaybackStateActions.OnPlaybackStateChanged(PlaybackState.PlaybackReady)
                )
            }

            override fun mediaChanged(mediaPlayer: MediaPlayer, media: MediaRef?) {
                onPlaybackStateAction(
                    PlaybackStateActions.OnMediaItemTransition(
                        mediaTitle = "",
                        index = 1
                    )
                )
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
                    onPlaybackStateAction(
                        PlaybackStateActions.OnIsPlayingChanged(isPlaying)
                    )
                }
            }

            override fun paused(mediaPlayer: MediaPlayer) {
                mediaPlayer.status().isPlaying.let { isPlaying ->
                    onPlaybackStateAction(
                        PlaybackStateActions.OnIsPlayingChanged(isPlaying)
                    )
                }
            }

            override fun stopped(mediaPlayer: MediaPlayer) {
                onPlaybackStateAction(
                    PlaybackStateActions.OnPlaybackStateChanged(PlaybackState.PlaybackEnded)
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
        KLog.w("testing LaunchedEffect started")
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
        KLog.w("init RenderState")
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
            KLog.e("updateComposeImage exception ${ex.localizedMessage}")
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