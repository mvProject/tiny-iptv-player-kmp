package com.mvproject.tinyiptvkmp.ui.screens.player.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.ui.components.PlayerView
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackStateActions
import com.mvproject.tinyiptvkmp.ui.screens.player.state.VideoViewState

@Composable
fun PlayerContainer(
    modifier: Modifier,
    videoViewState: VideoViewState,
    onPlaybackAction: (PlaybackActions) -> Unit,
    onPlaybackStateAction: (PlaybackStateActions) -> Unit,
    toolbar: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(videoViewState.videoRatio)
        ) {
            PlayerView(
                videoViewState = videoViewState,
                onPlaybackAction = onPlaybackAction,
                onPlaybackStateAction = onPlaybackStateAction,
            )
        }

        toolbar()
    }
}