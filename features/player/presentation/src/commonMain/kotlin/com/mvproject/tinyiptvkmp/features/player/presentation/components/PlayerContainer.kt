package com.mvproject.tinyiptvkmp.features.player.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.core.foundation.model.VideoSize
import com.mvproject.tinyiptvkmp.features.player.presentation.PlayerAction
import com.mvproject.tinyiptvkmp.features.player.presentation.PlayerState
import com.mvproject.tinyiptvkmp.platform.mediaplayer.MediaPlaybackState
import com.mvproject.tinyiptvkmp.platform.mediaplayer.MediaPlayerEvent
import com.mvproject.tinyiptvkmp.platform.mediaplayer.MediaPlayerState
import com.mvproject.tinyiptvkmp.platform.mediaplayer.MediaPlayerView

@Composable
fun PlayerContainer(
    modifier: Modifier,
    videoSize: VideoSize,
    mediaPlayerState: MediaPlayerState,
    onAction: (PlayerAction) -> Unit,
    toolbar: @Composable () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .adaptiveLayout(videoSize = videoSize)
        ) {
            MediaPlayerView(
                state = mediaPlayerState,
                onEvent = { event -> onAction(event.toPlayerUiAction()) }
            )
        }

        toolbar()
    }
}

private fun MediaPlayerEvent.toPlayerUiAction(): PlayerAction =
    when (this) {
        is MediaPlayerEvent.PlayingChanged -> PlayerAction.OnIsPlayingChanged(isPlaying)
        is MediaPlayerEvent.PlaybackStateChanged -> {
            PlayerAction.OnPlaybackStateChanged(state.toPlayerPlaybackState())
        }
    }

private fun MediaPlaybackState.toPlayerPlaybackState(): PlayerState.PlayerPlaybackState =
    when (this) {
        MediaPlaybackState.Buffering -> PlayerState.PlayerPlaybackState.PlaybackBuffering
        MediaPlaybackState.Ended -> PlayerState.PlayerPlaybackState.PlaybackEnded
        is MediaPlaybackState.Idle -> PlayerState.PlayerPlaybackState.PlaybackIdle(errorCode)
        MediaPlaybackState.Ready -> PlayerState.PlayerPlaybackState.PlaybackReady
    }
