/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 19.04.24, 16:05
 *
 */

package com.mvproject.tinyiptvkmp.platform.mediaplayer

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.retain.RetainObserver
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.mvproject.tinyiptvkmp.platform.mediaplayer.ExoPlayerUtils.createMediaItem
import com.mvproject.tinyiptvkmp.platform.mediaplayer.ExoPlayerUtils.createVideoPlayer
import com.mvproject.tinyiptvkmp.platform.mediaplayer.ExoPlayerUtils.mapToMediaPlaybackState
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

/**
 * Build and remember default implementation of [PlatformPlayerState]
 *
 * @param context used to build an [ExoPlayer] instance
 * */
@Composable
internal fun rememberPlayerState(
    context: Context = LocalContext.current,
    onEvent: (MediaPlayerEvent) -> Unit = {},
) = retain {
    val applicationContext = context.applicationContext
    val httpClient = HttpClient(OkHttp)
    PlayerStateImpl(
        player = createVideoPlayer(
            context = applicationContext,
            httpClient = httpClient,
        ),
        httpClient = httpClient,
        onEvent = onEvent,
    )
}.also { playerState ->
    playerState.updateEventSink(onEvent)
}

internal class PlayerStateImpl(
    val player: ExoPlayer,
    private val httpClient: HttpClient,
    onEvent: (MediaPlayerEvent) -> Unit = {},
) : PlatformPlayerState,
    Player.Listener,
    RetainObserver {
    private var onEvent: (MediaPlayerEvent) -> Unit = onEvent
    private var isReleased = false

    fun updateEventSink(onEvent: (MediaPlayerEvent) -> Unit) {
        this.onEvent = onEvent
    }

    override fun onRetained() {
        player.addListener(this)
    }

    override fun onEnteredComposition() = Unit

    override fun onExitedComposition() = Unit

    override fun onRetired() {
        release()
    }

    override fun onUnused() {
        release()
    }

    fun release() {
        if (isReleased) {
            return
        }
        isReleased = true
        player.removeListener(this)
        player.clearVideoSurface()
        player.release()
        httpClient.close()
    }

    override fun setVolume(value: Float) {
        if (isReleased) {
            return
        }
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
        if (isReleased) {
            return
        }
        if (value) {
            player.play()
        } else {
            player.pause()
        }
    }

    override fun play() {
        if (isReleased) {
            return
        }
        player.play()
    }

    override fun pause() {
        if (isReleased) {
            return
        }
        player.pause()
    }

    override fun restartPlayer() {
        if (isReleased) {
            return
        }
        if (this.player.playbackState == Player.STATE_IDLE) {
            this.player.apply {
                prepare()
                playWhenReady = true
            }
        }
    }

    override fun setPlayerChannel(channelUrl: String) {
        if (isReleased) {
            return
        }
        this.player.apply {
            setMediaItem(createMediaItem(url = channelUrl))
            prepare()
            playWhenReady = true
        }
    }
}
