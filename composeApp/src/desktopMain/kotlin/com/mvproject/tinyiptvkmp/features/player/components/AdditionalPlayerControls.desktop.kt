package com.mvproject.tinyiptvkmp.features.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.FeaturedPlayList
import androidx.compose.material.icons.automirrored.rounded.ViewList
import androidx.compose.material.icons.automirrored.rounded.VolumeDown
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.core.components.buttons.ControlButton
import com.mvproject.tinyiptvkmp.core.components.modifiers.SpacerWidth
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.features.player.PlayerUiAction
import com.mvproject.tinyiptvkmp.features.player.PlayerUiState

@Composable
actual fun AdditionalPlayerControls(
    modifier: Modifier,
    onClick: (PlayerUiAction) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        ControlButton(
            imageVector = Icons.Rounded.Close,
            onClick = { onClick(PlayerUiAction.NavigateBack) },
        )

        SpacerWidth(width = MaterialTheme.dimensionSize.size32)
        ControlButton(
            imageVector = Icons.AutoMirrored.Rounded.VolumeDown,
            onClick = { onClick(PlayerUiAction.VolumeDown) },
        )

        SpacerWidth(width = MaterialTheme.dimensionSize.size8)
        ControlButton(
            imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
            onClick = { onClick(PlayerUiAction.VolumeUp) },
        )

        SpacerWidth(width = MaterialTheme.dimensionSize.size24)
        ControlButton(
            imageVector = Icons.Rounded.SkipPrevious,
            onClick = { onClick(PlayerUiAction.SelectPrevious) },
        )

        SpacerWidth(width = MaterialTheme.dimensionSize.size8)
        ControlButton(
            imageVector = Icons.Rounded.SkipNext,
            onClick = { onClick(PlayerUiAction.SelectNext) },
        )

        SpacerWidth(width = MaterialTheme.dimensionSize.size24)
        ControlButton(
            imageVector = Icons.AutoMirrored.Rounded.ViewList,
            onClick = { onClick(PlayerUiAction.OpenOsd(PlayerUiState.PlayerOSD.ChannelPrograms)) },
        )

        SpacerWidth(width = MaterialTheme.dimensionSize.size8)
        ControlButton(
            imageVector = Icons.AutoMirrored.Rounded.FeaturedPlayList,
            onClick = { onClick(PlayerUiAction.OpenOsd(PlayerUiState.PlayerOSD.GroupChannels)) },
        )

        SpacerWidth(width = MaterialTheme.dimensionSize.size8)
        ControlButton(
            imageVector = Icons.Rounded.Info,
            onClick = { onClick(PlayerUiAction.OpenOsd(PlayerUiState.PlayerOSD.ProgramInfo)) },
        )

        SpacerWidth(width = MaterialTheme.dimensionSize.size24)
    }
}