package com.mvproject.tinyiptvkmp.features.player.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.core.common.utils.adaptiveLayout
import com.mvproject.tinyiptvkmp.features.player.PlayerUiAction
import com.mvproject.tinyiptvkmp.features.player.PlayerUiState

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
            PlayerView(
                uiState = uiState,
                onAction = onAction
            )
        }

        toolbar()
    }
}