package com.mvproject.tinyiptvkmp.features.player.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.features.player.PlayerContract.UiAction
import com.mvproject.tinyiptvkmp.features.player.PlayerContract.UiState

@Composable
expect fun PlayerView(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onUiAction: (UiAction) -> Unit = {},
)

