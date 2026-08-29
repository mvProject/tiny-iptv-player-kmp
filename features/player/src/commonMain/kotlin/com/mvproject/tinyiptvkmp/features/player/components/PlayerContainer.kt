package com.mvproject.tinyiptvkmp.features.player.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.features.player.PlayerUiAction
import com.mvproject.tinyiptvkmp.features.player.PlayerUiState
import com.mvproject.tinyiptvkmp.platform.mediaplayer.MediaPlaybackState
import com.mvproject.tinyiptvkmp.platform.mediaplayer.MediaPlayerEvent
import com.mvproject.tinyiptvkmp.platform.mediaplayer.MediaPlayerState
import com.mvproject.tinyiptvkmp.platform.mediaplayer.MediaPlayerView

@Composable
fun PlayerContainer(
    modifier: Modifier,
    uiState: PlayerUiState,
    onAction: (PlayerUiAction) -> Unit,
    toolbar: @Composable () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .adaptiveLayout(videoSize = uiState.videoSize)
        ) {
            MediaPlayerView(
                state = uiState.toMediaPlayerState(),
                onEvent = { event -> onAction(event.toPlayerUiAction()) }
            )
        }

        toolbar()
    }
}

private fun PlayerUiState.toMediaPlayerState() =
    MediaPlayerState(
        url = currentChannel.channelUrl,
        channelKey = channelIndex,
        volume = currentVolume,
        isPlaying = isPlaying,
    )

private fun MediaPlayerEvent.toPlayerUiAction(): PlayerUiAction =
    when (this) {
        is MediaPlayerEvent.PlayingChanged -> PlayerUiAction.OnIsPlayingChanged(isPlaying)
        is MediaPlayerEvent.PlaybackStateChanged -> {
            PlayerUiAction.OnPlaybackStateChanged(state.toPlayerPlaybackState())
        }
    }

private fun MediaPlaybackState.toPlayerPlaybackState(): PlayerUiState.PlayerPlaybackState =
    when (this) {
        MediaPlaybackState.Buffering -> PlayerUiState.PlayerPlaybackState.PlaybackBuffering
        MediaPlaybackState.Ended -> PlayerUiState.PlayerPlaybackState.PlaybackEnded
        is MediaPlaybackState.Idle -> PlayerUiState.PlayerPlaybackState.PlaybackIdle(errorCode)
        MediaPlaybackState.Ready -> PlayerUiState.PlayerPlaybackState.PlaybackReady
    }
