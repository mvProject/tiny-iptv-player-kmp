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
import com.mvproject.tinyiptvkmp.core.theme.dimens
import com.mvproject.tinyiptvkmp.core.ui.buttons.ControlButton
import com.mvproject.tinyiptvkmp.core.ui.modifiers.SpacerWidth

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

        SpacerWidth(width = MaterialTheme.dimens.size32)
        ControlButton(
            imageVector = Icons.AutoMirrored.Rounded.VolumeDown,
            onClick = { onClick(PlayerUiAction.VolumeDown) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size8)
        ControlButton(
            imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
            onClick = { onClick(PlayerUiAction.VolumeUp) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size24)
        ControlButton(
            imageVector = Icons.Rounded.SkipPrevious,
            onClick = { onClick(PlayerUiAction.SelectPrevious) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size8)
        ControlButton(
            imageVector = Icons.Rounded.SkipNext,
            onClick = { onClick(PlayerUiAction.SelectNext) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size24)
        ControlButton(
            imageVector = Icons.AutoMirrored.Rounded.ViewList,
            onClick = { onClick(PlayerUiAction.ToggleProgramsUi) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size8)
        ControlButton(
            imageVector = Icons.AutoMirrored.Rounded.FeaturedPlayList,
            onClick = { onClick(PlayerUiAction.ToggleChannelsUi) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size8)
        ControlButton(
            imageVector = Icons.Rounded.Info,
            onClick = { onClick(PlayerUiAction.ToggleProgramInfoUi) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size24)
    }
}