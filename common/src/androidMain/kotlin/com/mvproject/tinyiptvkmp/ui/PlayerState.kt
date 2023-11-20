/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 17:18
 *
 */

package com.mvproject.tinyiptvkmp.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.exoplayer.ExoPlayer
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackStateActions
import com.mvproject.tinyiptvkmp.utils.ExoPlayerUtils.createMediaItem
import com.mvproject.tinyiptvkmp.utils.ExoPlayerUtils.createVideoPlayer
import com.mvproject.tinyiptvkmp.utils.ExoPlayerUtils.mapToVideoPlaybackState

/**
 * Build and remember default implementation of [PlayerState]
 *
 * @param context used to build an [ExoPlayer] instance
 * */
@Composable
internal fun rememberPlayerState(
    context: Context = LocalContext.current,
    onPlaybackStateAction: (PlaybackStateActions) -> Unit = {}
) = remember {

    PlayerStateImpl(
        player = createVideoPlayer(context),
        onPlaybackStateAction = onPlaybackStateAction
    ).also { playerState ->
        playerState.player.apply {
            addListener(playerState)
        }
    }
}

class PlayerStateImpl(
    override val player: ExoPlayer,
    private val onPlaybackStateAction: (PlaybackStateActions) -> Unit = {}
) : PlayerState, Player.Listener {

    override fun setVolume(value: Float) {
        player.volume = value
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        val data = mediaItem?.mediaMetadata?.displayTitle.toString()
        onPlaybackStateAction(
            PlaybackStateActions.OnMediaItemTransition(
                mediaTitle = data,
                index = player.currentMediaItemIndex
            )
        )
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        onPlaybackStateAction(
            PlaybackStateActions.OnIsPlayingChanged(isPlaying)
        )
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        /*        val state = when (playbackState) {
                    Player.STATE_IDLE -> {
                        VideoPlaybackState.VideoPlaybackIdle(errorCode = player.playerError?.errorCode)
                    }

                    Player.STATE_BUFFERING -> VideoPlaybackState.VideoPlaybackBuffering
                    Player.STATE_ENDED -> VideoPlaybackState.VideoPlaybackEnded
                    else -> VideoPlaybackState.VideoPlaybackReady
                }*/

        val state = mapToVideoPlaybackState(
            playbackState = playbackState,
            errorCode = player.playerError?.errorCode
        )

        onPlaybackStateAction(
            PlaybackStateActions.OnPlaybackStateChanged(state)
        )
    }

    /*    override fun onTracksChanged(tracks: Tracks) {

            tracks.groups.forEachIndexed { _, group ->
                if (group.type == C.TRACK_TYPE_AUDIO) {
                    for (i in 0..group.length - 1) {
                        val trackFormat = group.getTrackFormat(i)
                        val audioTrackLanguage = trackFormat.language.toString()
                        val audioTrackLabel = trackFormat.label.toString()
                        Napier.i("testing group trackFormat $trackFormat")
                        Napier.i("testing group audioTrackLanguage $audioTrackLanguage")
                        Napier.i("testing group audioTrackLabel $audioTrackLabel")
                        }
                    }
                }
        }*/

    override fun onVideoSizeChanged(videoSize: VideoSize) {
        onPlaybackStateAction(
            PlaybackStateActions.OnVideoSizeChanged(
                videoSize.height,
                videoSize.width,
                videoSize.pixelWidthHeightRatio
            )
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

    override fun setPlayerChannel(
        channelName: String,
        channelUrl: String
    ) {
        this.player.apply {
            setMediaItem(
                createMediaItem(title = channelName, url = channelUrl)
                /*               MediaItem.Builder()
                                   .setUri(channelUrl)
                                   .setMediaMetadata(
                                       MediaMetadata.Builder()
                                           .setDisplayTitle(channelName)
                                           .build()
                                   ).build()*/
            )
            prepare()
            playWhenReady = true
        }
    }
}

interface PlayerState {

    val player: ExoPlayer
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