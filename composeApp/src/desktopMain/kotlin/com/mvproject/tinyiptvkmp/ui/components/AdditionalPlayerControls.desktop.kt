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
import com.mvproject.tinyiptvkmp.ui.components.modifiers.SpacerWidth
import com.mvproject.tinyiptvkmp.ui.components.views.PlaybackControl
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
        PlaybackControl(
            imageVector = Icons.Rounded.Close,
            action = action,
        )

        SpacerWidth(width = MaterialTheme.dimens.size32)
        PlaybackControl(
            imageVector = Icons.AutoMirrored.Rounded.VolumeDown,
            action = { onPlaybackAction(PlaybackActions.OnVolumeDown) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size8)
        PlaybackControl(
            imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
            action = { onPlaybackAction(PlaybackActions.OnVolumeUp) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size24)
        PlaybackControl(
            imageVector = Icons.Rounded.SkipPrevious,
            action = { onPlaybackAction(PlaybackActions.OnPreviousSelected) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size8)
        PlaybackControl(
            imageVector = Icons.Rounded.SkipNext,
            action = { onPlaybackAction(PlaybackActions.OnNextSelected) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size24)
        PlaybackControl(
            imageVector = Icons.AutoMirrored.Rounded.ViewList,
            action = { onPlaybackAction(PlaybackActions.OnEpgUiToggle) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size8)
        PlaybackControl(
            imageVector = Icons.AutoMirrored.Rounded.FeaturedPlayList,
            action = { onPlaybackAction(PlaybackActions.OnChannelsUiToggle) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size8)
        PlaybackControl(
            imageVector = Icons.Rounded.Info,
            action = { onPlaybackAction(PlaybackActions.OnChannelInfoUiToggle) },
        )

        SpacerWidth(width = MaterialTheme.dimens.size24)
    }
}