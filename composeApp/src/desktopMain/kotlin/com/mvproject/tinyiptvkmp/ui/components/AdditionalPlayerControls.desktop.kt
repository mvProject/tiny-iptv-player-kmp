package com.mvproject.tinyiptvkmp.ui.components

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
import com.mvproject.tinyiptvkmp.ui.components.buttons.ControlButton
import com.mvproject.tinyiptvkmp.ui.components.modifiers.SpacerWidth
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptvkmp.ui.theme.dimens

@Composable
actual fun AdditionalPlayerControls(
    modifier: Modifier,
    action: () -> Unit,
    onPlaybackAction: (PlaybackActions) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        ControlButton(
            imageVector = Icons.Rounded.Close,
            onClick = action,
        )

        SpacerWidth(width = MaterialTheme.dimens.size32)
        ControlButton(
            imageVector = Icons.AutoMirrored.Rounded.VolumeDown,
            onClick = { onPlaybackAction(PlaybackActions.OnVolumeDown) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size8)
        ControlButton(
            imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
            onClick = { onPlaybackAction(PlaybackActions.OnVolumeUp) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size24)
        ControlButton(
            imageVector = Icons.Rounded.SkipPrevious,
            onClick = { onPlaybackAction(PlaybackActions.OnPreviousSelected) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size8)
        ControlButton(
            imageVector = Icons.Rounded.SkipNext,
            onClick = { onPlaybackAction(PlaybackActions.OnNextSelected) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size24)
        ControlButton(
            imageVector = Icons.AutoMirrored.Rounded.ViewList,
            onClick = { onPlaybackAction(PlaybackActions.OnEpgUiToggle) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size8)
        ControlButton(
            imageVector = Icons.AutoMirrored.Rounded.FeaturedPlayList,
            onClick = { onPlaybackAction(PlaybackActions.OnChannelsUiToggle) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size8)
        ControlButton(
            imageVector = Icons.Rounded.Info,
            onClick = { onPlaybackAction(PlaybackActions.OnChannelInfoUiToggle) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size24)
    }
}