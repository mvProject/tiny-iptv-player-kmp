package com.mvproject.tinyiptvkmp.features.player.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.features.player.action.PlaybackActions
import com.mvproject.tinyiptvkmp.features.player.action.PlaybackStateActions
import com.mvproject.tinyiptvkmp.features.player.state.TvPlayerState

@Composable
fun PlayerContainer(
    modifier: Modifier,
    tvPlayerState: TvPlayerState,
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
                .aspectRatio(tvPlayerState.videoRatio)
        ) {
            PlayerView(
                tvPlayerState = tvPlayerState,
                onPlaybackAction = onPlaybackAction,
                onPlaybackStateAction = onPlaybackStateAction,
            )
        }

        toolbar()
    }
}