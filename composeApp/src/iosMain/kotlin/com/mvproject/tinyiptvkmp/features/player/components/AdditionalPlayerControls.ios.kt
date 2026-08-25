package com.mvproject.tinyiptvkmp.features.player.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.core.components.buttons.ControlButton
import com.mvproject.tinyiptvkmp.features.player.PlayerUiAction

@Composable
actual fun AdditionalPlayerControls(
    modifier: Modifier,
    onClick: (PlayerUiAction) -> Unit
) {
    ControlButton(
        imageVector = Icons.Rounded.Close,
        onClick = { onClick(PlayerUiAction.NavigateBack) },
    )
}