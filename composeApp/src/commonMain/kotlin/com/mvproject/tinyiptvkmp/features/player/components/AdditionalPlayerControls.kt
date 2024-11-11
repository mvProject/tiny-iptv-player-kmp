package com.mvproject.tinyiptvkmp.features.player.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.features.player.PlayerUiAction

@Composable
expect fun AdditionalPlayerControls(
    modifier: Modifier,
    onClick: (PlayerUiAction) -> Unit
)