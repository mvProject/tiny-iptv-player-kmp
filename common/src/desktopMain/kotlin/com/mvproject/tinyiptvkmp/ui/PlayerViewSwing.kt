/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 17:18
 *
 */

package com.mvproject.tinyiptvkmp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import co.touchlab.kermit.Logger.Companion.w
import com.mvproject.tinyiptvkmp.ui.components.modifiers.adaptiveLayout
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackStateActions
import com.mvproject.tinyiptvkmp.ui.screens.player.state.VideoPlaybackState
import com.mvproject.tinyiptvkmp.ui.screens.player.state.VideoViewState
import com.mvproject.tinyiptvkmp.utils.AppConstants
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
        playerState.setVolume(videoViewState.currentVolume)
    }

    LaunchedEffect(videoViewState.mediaPosition) {
        if (videoViewState.mediaPosition > AppConstants.INT_NO_VALUE) {
            playerState.setPlayerChannel(
                channelName = videoViewState.currentChannel.channelName,
                channelUrl = videoViewState.currentChannel.channelUrl
            )
        }
    }

    LaunchedEffect(videoViewState.isPlaying) {
        playerState.setPlayingState(videoViewState.isPlaying)
        w {
            "testing isPlaying:${videoViewState.isPlaying}"
        }
    }

    //NativeLibrary.addSearchPath(
    //    RuntimeUtil.getLibVlcLibraryName(),
    //    "C:/Program Files/VideoLAN/VLC"
    //)
    //Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc::class.java)

    NativeDiscovery().discover()

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        Row(modifier = Modifier.weight(10f)) {
            SwingPanel(
                factory = { playerState.playerComponent },
                background = Color.Transparent,
                modifier = Modifier
                    .adaptiveLayout(
                        aspectRatio = videoViewState.videoRatio,
                        resizeMode = videoViewState.videoResizeMode
                    )
            )
        }

        Row(modifier = Modifier.weight(2f)) {
            controls()
        }
    }

    DisposableEffect(playerState) {
        onDispose {
            playerState.closePlayer()
        }
    }
}

@Composable
internal fun rememberPlayerStateSwing(
    onPlaybackStateAction: (PlaybackStateActions) -> Unit = {}
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
        playerComponent.mediaPlayer().controls().stop()
        playerComponent.mediaPlayer().events().removeMediaPlayerEventListener(this)
        playerComponent.mediaPlayer()::release
    }

    override fun error(mediaPlayer: MediaPlayer?) {}
    override fun mediaPlayerReady(mediaPlayer: MediaPlayer?) {
        onPlaybackStateAction(
            PlaybackStateActions.OnPlaybackStateChanged(VideoPlaybackState.VideoPlaybackReady)
        )
    }

    override fun mediaChanged(mediaPlayer: MediaPlayer?, media: MediaRef?) {
        onPlaybackStateAction(
            PlaybackStateActions.OnMediaItemTransition(
                mediaTitle = "",
                index = 1
            )
        )
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
        mediaPlayer?.status()?.isPlaying?.let { isPlaying ->
            onPlaybackStateAction(
                PlaybackStateActions.OnIsPlayingChanged(isPlaying)
            )
        }
    }

    override fun paused(mediaPlayer: MediaPlayer?) {
        mediaPlayer?.status()?.isPlaying?.let { isPlaying ->
            onPlaybackStateAction(
                PlaybackStateActions.OnIsPlayingChanged(isPlaying)
            )
        }
    }

    override fun stopped(mediaPlayer: MediaPlayer?) {
        println("stopped")
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