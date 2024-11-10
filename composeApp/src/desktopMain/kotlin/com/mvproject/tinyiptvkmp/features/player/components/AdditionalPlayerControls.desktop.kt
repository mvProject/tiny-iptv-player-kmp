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
import com.mvproject.tinyiptvkmp.features.player.PlayerContract.UiAction

@Composable
actual fun AdditionalPlayerControls(
    modifier: Modifier,
    onClick: (UiAction) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        ControlButton(
            imageVector = Icons.Rounded.Close,
            onClick = { onClick(UiAction.NavigateBack) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size32)
        ControlButton(
            imageVector = Icons.AutoMirrored.Rounded.VolumeDown,
            onClick = { onClick(UiAction.VolumeDown) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size8)
        ControlButton(
            imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
            onClick = { onClick(UiAction.VolumeUp) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size24)
        ControlButton(
            imageVector = Icons.Rounded.SkipPrevious,
            onClick = { onClick(UiAction.SelectPrevious) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size8)
        ControlButton(
            imageVector = Icons.Rounded.SkipNext,
            onClick = { onClick(UiAction.SelectNext) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size24)
        ControlButton(
            imageVector = Icons.AutoMirrored.Rounded.ViewList,
            onClick = { onClick(UiAction.ToggleProgramsUi) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size8)
        ControlButton(
            imageVector = Icons.AutoMirrored.Rounded.FeaturedPlayList,
            onClick = { onClick(UiAction.ToggleChannelsUi) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size8)
        ControlButton(
            imageVector = Icons.Rounded.Info,
            onClick = { onClick(UiAction.ToggleProgramInfoUi) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size24)
    }
}