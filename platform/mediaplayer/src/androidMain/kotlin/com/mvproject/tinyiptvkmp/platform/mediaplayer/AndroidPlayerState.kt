/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 19.04.24, 16:05
 *
 */

package com.mvproject.tinyiptvkmp.platform.mediaplayer

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.mvproject.tinyiptvkmp.platform.mediaplayer.ExoPlayerUtils.createMediaItem
import com.mvproject.tinyiptvkmp.platform.mediaplayer.ExoPlayerUtils.createVideoPlayer
import com.mvproject.tinyiptvkmp.platform.mediaplayer.ExoPlayerUtils.mapToMediaPlaybackState

/**
 * Build and remember default implementation of [PlatformPlayerState]
 *
 * @param context used to build an [ExoPlayer] instance
 * */
@Composable
internal fun rememberPlayerState(
    context: Context = LocalContext.current,
    onEvent: (MediaPlayerEvent) -> Unit = {},
) = remember {
    PlayerStateImpl(
        player = createVideoPlayer(context),
        onEvent = onEvent,
    ).also { playerState ->
        playerState.player.apply {
            addListener(playerState)
        }
    }
}

internal class PlayerStateImpl(
    val player: ExoPlayer,
    private val onEvent: (MediaPlayerEvent) -> Unit = {},
) : PlatformPlayerState, Player.Listener {
    override fun setVolume(value: Float) {
        player.volume = value
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        onEvent(
            MediaPlayerEvent.PlayingChanged(isPlaying),
        )
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        val state =
            mapToMediaPlaybackState(
                playbackState = playbackState,
                errorCode = player.playerError?.errorCode,
            )

        onEvent(
            MediaPlayerEvent.PlaybackStateChanged(state),
        )
    }

    override fun setPlayingState(value: Boolean) {
        if (value) {
            player.play()
        } else {
            player.pause()
        }
    }

    override fun play() {
        player.play()
    }

    override fun pause() {
        player.pause()
    }

    override fun restartPlayer() {
        if (this.player.playbackState == Player.STATE_IDLE) {
            this.player.apply {
                prepare()
                playWhenReady = true
            }
        }
    }

    override fun setPlayerChannel(channelUrl: String) {
        this.player.apply {
            setMediaItem(createMediaItem(url = channelUrl))
            prepare()
            playWhenReady = true
        }
    }
}
