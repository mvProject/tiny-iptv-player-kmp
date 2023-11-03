/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 01.11.23, 12:44
 *
 */

package com.mvproject.tinyiptv.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import com.mvproject.tinyiptv.ui.components.modifiers.adaptiveLayout
import com.mvproject.tinyiptv.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptv.ui.screens.player.action.PlaybackStateActions
import com.mvproject.tinyiptv.ui.screens.player.state.VideoPlaybackState
import com.mvproject.tinyiptv.ui.screens.player.state.VideoViewState
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.AppConstants
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery
import uk.co.caprica.vlcj.media.MediaRef
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter
import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent


@Composable
internal fun PlayerViewSwing(
    modifier: Modifier = Modifier,
    videoViewState: VideoViewState,
    onPlaybackAction: (PlaybackActions) -> Unit = {},
    onPlaybackStateAction: (PlaybackStateActions) -> Unit = {},
    controls: @Composable () -> Unit
) {
    // todo network Available check

    // todo implement some player state Available check
    val playerState = rememberPlayerStateSwing(
        onPlaybackStateAction = onPlaybackStateAction
    )

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

        NativeDiscovery().discover()

        SwingPanel(
            factory = { playerState.playerComponent },
            background = Color.Transparent,
            modifier = Modifier
                .padding(vertical = MaterialTheme.dimens.size78)
                .adaptiveLayout(
                    aspectRatio = videoViewState.videoSizeRatio,
                    resizeMode = videoViewState.videoResizeMode
                )
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
internal fun rememberPlayerStateSwing(
    onPlaybackStateAction: (PlaybackStateActions) -> Unit = {}
) = remember {
    println("rememberPlayerState")
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
    override val playerComponent: CallbackMediaPlayerComponent,
    private val onPlaybackStateAction: (PlaybackStateActions) -> Unit = {}
) : PlayerStateSwing, MediaPlayerEventAdapter() {

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

interface PlayerStateSwing {
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