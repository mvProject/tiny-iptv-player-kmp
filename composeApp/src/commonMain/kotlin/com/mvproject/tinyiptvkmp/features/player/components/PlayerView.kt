package com.mvproject.tinyiptvkmp.features.player.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.features.player.PlayerUiAction
import com.mvproject.tinyiptvkmp.features.player.PlayerUiState

@Composable
expect fun PlayerView(
    modifier: Modifier = Modifier,
    uiState: PlayerUiState,
    onUiAction: (PlayerUiAction) -> Unit = {},
)

