package com.mvproject.tinyiptvkmp.platform.mediaplayer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class MediaPlayerState(
    val url: String,
    val channelKey: Int,
    val volume: Float,
    val isPlaying: Boolean,
)

sealed interface MediaPlayerEvent {
    data class PlayingChanged(val isPlaying: Boolean) : MediaPlayerEvent
    data class PlaybackStateChanged(val state: MediaPlaybackState) : MediaPlayerEvent
}

sealed interface MediaPlaybackState {
    data object Ready : MediaPlaybackState
    data object Ended : MediaPlaybackState
    data object Buffering : MediaPlaybackState
    data class Idle(val errorCode: Int?) : MediaPlaybackState
}

@Composable
expect fun MediaPlayerView(
    modifier: Modifier = Modifier,
    state: MediaPlayerState,
    onEvent: (MediaPlayerEvent) -> Unit = {},
)
