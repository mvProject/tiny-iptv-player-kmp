package com.mvproject.tinyiptvkmp.features.player.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.features.player.action.UiActions

@Composable
expect fun AdditionalPlayerControls(
    modifier: Modifier,
    action: () -> Unit,
    onClick: (UiActions) -> Unit,
)