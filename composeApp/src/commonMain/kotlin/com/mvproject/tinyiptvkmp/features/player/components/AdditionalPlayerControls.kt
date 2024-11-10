package com.mvproject.tinyiptvkmp.features.player.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.features.player.PlayerContract.UiAction

@Composable
expect fun AdditionalPlayerControls(
    modifier: Modifier,
    onClick: (UiAction) -> Unit
)