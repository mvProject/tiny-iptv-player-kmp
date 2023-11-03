/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 01.11.23, 12:44
 *
 */

package com.mvproject.tinyiptv.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.window.rememberWindowState
import com.mvproject.tinyiptv.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptv.ui.screens.player.action.PlaybackStateActions
import com.mvproject.tinyiptv.ui.screens.player.state.VideoPlaybackState
import com.mvproject.tinyiptv.ui.screens.player.state.VideoViewState
import com.mvproject.tinyiptv.utils.AppConstants
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ImageInfo
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery
import uk.co.caprica.vlcj.media.MediaRef
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter
import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent
import uk.co.caprica.vlcj.player.embedded.videosurface.CallbackVideoSurface
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurfaceAdapters
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.format.RV32BufferFormat
import java.nio.ByteBuffer
import javax.swing.JComponent


@Composable
internal fun PlayerViewComponent(
    modifier: Modifier = Modifier,
    videoViewState: VideoViewState,
    onPlaybackAction: (PlaybackActions) -> Unit = {},
    onPlaybackStateAction: (PlaybackStateActions) -> Unit = {},
    controls: @Composable () -> Unit
) {
    // todo network Available check

    // todo implement some player state Available check
    val playerState = rememberPlayerStateComponent(
        onPlaybackStateAction = onPlaybackStateAction
    )

    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    LaunchedEffect(videoViewState.isRestartRequired) {
        if (videoViewState.isRestartRequired) {
            // todo player restart
            playerState.restartPlayer()
            onPlaybackAction(PlaybackActions.OnRestarted)
        }
    }

    LaunchedEffect(videoViewState.isFullscreen) {
        // todo handle fullscreen state
    }

    LaunchedEffect(videoViewState.currentVolume) {
        // todo player setVolume
        playerState.setVolume(videoViewState.currentVolume)
    }

    LaunchedEffect(videoViewState.mediaPosition) {
        if (videoViewState.mediaPosition > AppConstants.INT_NO_VALUE) {
            // todo player setPlayerChannel
            playerState.setPlayerChannel(
                channelName = videoViewState.currentChannel.channelName,
                channelUrl = videoViewState.currentChannel.channelUrl
            )
        }
    }

    LaunchedEffect(videoViewState.isPlaying) {
        // todo player setPlayingState
        playerState.setPlayingState(videoViewState.isPlaying)
    }

    Box(
        modifier = modifier.fillMaxSize(),
    ) {

        //NativeLibrary.addSearchPath(
        //    RuntimeUtil.getLibVlcLibraryName(),
        //    "C:/Program Files/VideoLAN/VLC"
        //)
        //Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc::class.java)

        //    NativeDiscovery().discover()

        VideoPlayer(
            url = videoViewState.currentChannel.channelUrl,
            modifier = Modifier
                .fillMaxSize(),
            //.onPointerEvent(PointerEventType.Press) {
            //    state = state.copy(showControls = true)
            //    scope.launch {
            //        delay(3000)
            //        state = state.copy(showControls = false)
            //    }
            //},
            onPlayerReady = {

            }
        )

        controls()
    }

    DisposableEffect(Unit) {
        onDispose {
            println("DisposableEffect Unit")
            playerState.playerComponent.mediaPlayer()::release
        }
    }

    DisposableEffect(playerState) {
        onDispose {
            println("DisposableEffect playerState")
            //playerState.playerComponent.mediaPlayer()::release
        }
    }
}

@Composable
internal fun rememberPlayerStateComponent(
    onPlaybackStateAction: (PlaybackStateActions) -> Unit = {}
) = remember {
    println("rememberPlayerState")
    PlayerStateComponentImpl(
        playerComponent = CallbackMediaPlayerComponent(),
        onPlaybackStateAction = onPlaybackStateAction
    ).also { playerState ->
        playerState.playerComponent.apply {
            mediaPlayer().events().addMediaPlayerEventListener(playerState)
        }
    }
}

class PlayerStateComponentImpl(
    override val playerComponent: CallbackMediaPlayerComponent,
    private val onPlaybackStateAction: (PlaybackStateActions) -> Unit = {}
) : PlayerStateComponent, MediaPlayerEventAdapter() {

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
        playerComponent.mediaPlayer().media().play(channelUrl)
    }

    override fun closePlayer(
    ) {
        // playerComponent.mediaPlayer().events().removeMediaPlayerEventListener(this)
        playerComponent.mediaPlayer()::release
    }

    override fun volumeChanged(mediaPlayer: MediaPlayer?, volume: Float) {
        println("volumeChanged $volume")
    }

    override fun error(mediaPlayer: MediaPlayer?) {}
    override fun mediaPlayerReady(mediaPlayer: MediaPlayer?) {
        println("mediaPlayerReady")
        onPlaybackStateAction(
            PlaybackStateActions.OnMediaItemTransition(
                mediaTitle = "",
                index = 1
            )
        )
    }

    override fun mediaChanged(mediaPlayer: MediaPlayer?, media: MediaRef?) {
        println("mediaChanged")
        onPlaybackStateAction(
            PlaybackStateActions.OnPlaybackStateChanged(VideoPlaybackState.VideoPlaybackReady)
        )
    }

    override fun opening(mediaPlayer: MediaPlayer?) {
        println("opening")
    }

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
        val isPlaying = mediaPlayer?.status()?.isPlaying
        println("playing $isPlaying")
        onPlaybackStateAction(
            PlaybackStateActions.OnIsPlayingChanged(true)
        )
    }

    override fun paused(mediaPlayer: MediaPlayer?) {
        val isPlaying = mediaPlayer?.status()?.isPlaying
        println("paused $isPlaying")
        onPlaybackStateAction(
            PlaybackStateActions.OnIsPlayingChanged(false)
        )
    }

    override fun stopped(mediaPlayer: MediaPlayer?) {
        println("stopped")
        onPlaybackStateAction(
            PlaybackStateActions.OnPlaybackStateChanged(VideoPlaybackState.VideoPlaybackIdle(0))
        )
    }

    override fun finished(mediaPlayer: MediaPlayer?) {
        println("finished")
        onPlaybackStateAction(
            PlaybackStateActions.OnPlaybackStateChanged(VideoPlaybackState.VideoPlaybackEnded)
        )
    }
}

interface PlayerStateComponent {
    val playerComponent: CallbackMediaPlayerComponent
    fun play()
    fun pause()
    fun setVolume(value: Float)
    fun setPlayingState(value: Boolean)
    fun restartPlayer()
    fun setPlayerChannel(
        channelName: String,
        channelUrl: String
    )

    fun closePlayer()
}


@Composable
fun VideoPlayer(
    url: String,
    modifier: Modifier = Modifier,
    onPlayerReady: (MediaPlayer) -> Unit = {},
    onPlayerFinish: () -> Unit = {},
    onTimeChanged: (Long) -> Unit = {},
    onBuffer: () -> Unit = {}
) {
    val windowState = rememberWindowState().size
    NativeDiscovery().discover()
    var imageBitmap by remember {
        mutableStateOf(
            ImageBitmap(
                windowState.width.value.toInt(),
                windowState.height.value.toInt()
            )
        )
    }
    // val mediaPlayerComponent = rememberMediaPlayerComponent()
    val mediaPlayerComponent = rememberPlayerStateComponent()

    Image(
        modifier = modifier
            .background(Color.Transparent),
        bitmap = imageBitmap,
        contentDescription = "Video"
    )

    DisposableEffect(Unit) {
        var byteArray: ByteArray? = null
        var imageInfo: ImageInfo? = null
        val mediaPlayer = mediaPlayerComponent.playerComponent.mediaPlayer()
        val callbackVideoSurface = CallbackVideoSurface(
            object : BufferFormatCallback {

                override fun getBufferFormat(sourceWidth: Int, sourceHeight: Int): BufferFormat {
                    imageInfo = ImageInfo.makeN32(sourceWidth, sourceHeight, ColorAlphaType.OPAQUE)
                    return RV32BufferFormat(sourceWidth, sourceHeight)
                }

                override fun allocatedBuffers(buffers: Array<out ByteBuffer>) {
                    byteArray = ByteArray(buffers[0].limit())
                }
            },
            { _, nativeBuffers, _ ->
                imageInfo?.let {
                    val byteBuffer = nativeBuffers[0]
                    byteBuffer.get(byteArray)
                    byteBuffer.rewind()
                    imageBitmap = Bitmap().apply {
                        allocPixels(it)
                        installPixels(byteArray)
                    }.asComposeImageBitmap()
                }
            },
            true,
            VideoSurfaceAdapters.getVideoSurfaceAdapter(),
        )

        mediaPlayer.videoSurface().set(callbackVideoSurface)

        mediaPlayer.events().addMediaPlayerEventListener(object : MediaPlayerEventAdapter() {

            override fun mediaPlayerReady(mediaPlayer: MediaPlayer) {
                super.mediaPlayerReady(mediaPlayer)
                onPlayerReady(mediaPlayer)
            }

            override fun finished(mediaPlayer: MediaPlayer) {
                super.finished(mediaPlayer)
                onPlayerFinish()
            }

            override fun timeChanged(mediaPlayer: MediaPlayer, newTime: Long) {
                super.timeChanged(mediaPlayer, newTime)
                onTimeChanged(newTime)
            }

            override fun buffering(mediaPlayer: MediaPlayer?, newCache: Float) {
                super.buffering(mediaPlayer, newCache)
                onBuffer()
            }


            override fun error(mediaPlayer: MediaPlayer?) {
                super.error(mediaPlayer)
                println("Error occurred")
            }
        })
        mediaPlayer.media().play(url)
        onDispose {
            mediaPlayer.release()
        }
    }
}

/*private fun Any.mediaPlayer(): EmbeddedMediaPlayer {
    return when (this) {
        is CallbackMediaPlayerComponent -> mediaPlayer()
        is EmbeddedMediaPlayerComponent -> mediaPlayer()
        else -> throw IllegalArgumentException("You can only call mediaPlayer() on vlcj player component")
    }
}*/
@Composable
fun rememberMediaPlayerComponent(): JComponent {
    return remember {
        CallbackMediaPlayerComponent()
    }
}