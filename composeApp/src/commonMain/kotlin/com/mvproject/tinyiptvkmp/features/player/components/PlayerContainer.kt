package com.mvproject.tinyiptvkmp.features.player.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.features.player.action.UiActions
import com.mvproject.tinyiptvkmp.features.player.state.TvPlayerState

@Composable
fun PlayerContainer(
    modifier: Modifier,
    tvPlayerState: TvPlayerState,
    onUiAction: (UiActions) -> Unit,
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
                onUiAction = onUiAction
            )
        }

        toolbar()
    }
}