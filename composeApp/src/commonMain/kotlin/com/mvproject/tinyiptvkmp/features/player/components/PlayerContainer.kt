package com.mvproject.tinyiptvkmp.features.player.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.features.player.PlayerContract.UiAction
import com.mvproject.tinyiptvkmp.features.player.PlayerContract.UiState

@Composable
fun PlayerContainer(
    modifier: Modifier,
    uiState: UiState,
    onUiAction: (UiAction) -> Unit,
    toolbar: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(uiState.videoRatio)
        ) {
            PlayerView(
                uiState = uiState,
                onUiAction = onUiAction
            )
        }

        toolbar()
    }
}