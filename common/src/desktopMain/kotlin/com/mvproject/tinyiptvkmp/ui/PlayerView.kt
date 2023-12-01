/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 30.11.23, 18:44
 *
 */

package com.mvproject.tinyiptvkmp.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.drawscope.scale
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackStateActions
import com.mvproject.tinyiptvkmp.ui.screens.player.state.VideoPlaybackState
import com.mvproject.tinyiptvkmp.ui.screens.player.state.VideoViewState
import com.mvproject.tinyiptvkmp.utils.AppConstants
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
import kotlin.math.min


@Composable
internal fun PlayerView(
    modifier: Modifier = Modifier,
    videoViewState: VideoViewState,
    onPlaybackAction: (PlaybackActions) -> Unit = {},
    onPlaybackStateAction: (PlaybackStateActions) -> Unit = {},
    controls: @Composable () -> Unit
) {
    // todo network Available check

    val videoPlayerState = remember { VideoPlayerStateImpl() }

    LaunchedEffect(videoViewState.isRestartRequired) {
        if (videoViewState.isRestartRequired) {
            // todo player restart
            videoPlayerState.restartPlayer()
            onPlaybackAction(PlaybackActions.OnRestarted)
        }
    }

    LaunchedEffect(videoViewState.isFullscreen) {
        // todo handle fullscreen state
    }

    LaunchedEffect(videoViewState.currentVolume) {
        videoPlayerState.setVolume(videoViewState.currentVolume)
    }

    LaunchedEffect(videoViewState.mediaPosition) {
        if (videoViewState.mediaPosition > AppConstants.INT_NO_VALUE) {
            videoPlayerState.setPlayerChannel(
                channelName = videoViewState.currentChannel.channelName,
                channelUrl = videoViewState.currentChannel.channelUrl
            )
        }
    }

    LaunchedEffect(videoViewState.isPlaying) {
        videoPlayerState.setPlayingState(videoViewState.isPlaying)
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        VideoPlayerDirect(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(videoPlayerState.aspectRatio),
            state = videoPlayerState,
            url = videoViewState.currentChannel.channelUrl,
            onPlaybackStateAction = onPlaybackStateAction
        )

        controls()
    }
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
                KLog.i("testing audioTracks Playing: ${mediaPlayer.media().info().audioTracks()}")
                onPlaybackStateAction(
                    PlaybackStateActions.OnPlaybackStateChanged(VideoPlaybackState.VideoPlaybackReady)
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
                    PlaybackStateActions.OnPlaybackStateChanged(VideoPlaybackState.VideoPlaybackEnded)
                )
            }
        }
        state.mediaPlayer.events().addMediaPlayerEventListener(eventListener)
        onDispose {
            state.mediaPlayer.events().removeMediaPlayerEventListener(eventListener)
            state.mediaPlayer.release()
        }
    }
    var frameTime: Long by remember { mutableStateOf(0L) }
    LaunchedEffect(url) {
        KLog.w("testing LaunchedEffect started")
        state.mediaPlayer.media()?.start(url)
        state.mediaPlayer.subpictures().setTrack(-1)
        while (true) {
            withFrameMillis {
                frameTime = it
            }
        }
    }
    return Canvas(modifier) {
        state.internalState.updateComposeImage(frameTime)?.let { image ->
            val scaleW = size.width / image.width.toFloat()
            val scaleH = size.height / image.height.toFloat()
            scale(
                scale = min(scaleH, scaleW),
                pivot = Offset.Zero
            ) {
                drawImage(image)
            }
        }
    }
}

internal class RenderState {
    var aspectRatio: Float by mutableStateOf(1f)
        private set

    var currentBuffer: ByteBuffer? = null

    private var buffer: ByteArray = ByteArray(0)
    private var bufferBitmap: Bitmap = Bitmap()
    private var composeImage: ImageBitmap? = null

    init {
        KLog.w("init RenderState")
    }

    fun updateComposeImage(frameTime: Long): ImageBitmap? {
        try {
            currentBuffer?.let {
                it.get(buffer)
                it.rewind()
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
            buffer = ByteArray(sourceWidth * sourceHeight * 4)
            with(bufferBitmap) {
                aspectRatio = if (width <= 0 || height <= 0)
                    1f
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