package com.mvproject.tinyiptvkmp.platform.mediaplayer

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mvproject.tinyiptvkmp.core.components.buttons.ControlButton
import com.mvproject.tinyiptvkmp.core.components.modifiers.SpacerWidth
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.Res
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.action_close
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.action_next_channel
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.action_open_channels
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.action_open_info
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.action_open_programs
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.action_previous_channel
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.action_volume_down
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.action_volume_up
import org.jetbrains.compose.resources.stringResource

@Composable
actual fun AdditionalMediaPlayerControls(
    modifier: Modifier,
    onNavigateBack: () -> Unit,
    onVolumeDown: () -> Unit,
    onVolumeUp: () -> Unit,
    onSelectPrevious: () -> Unit,
    onSelectNext: () -> Unit,
    onOpenPrograms: () -> Unit,
    onOpenChannels: () -> Unit,
    onOpenInfo: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        ControlButton(
            imageVector = Icons.Rounded.Close,
            contentDescription = stringResource(Res.string.action_close),
            onClick = onNavigateBack,
        )

        SpacerWidth(width = 32.dp)
        ControlButton(
            imageVector = Icons.AutoMirrored.Rounded.VolumeDown,
            contentDescription = stringResource(Res.string.action_volume_down),
            onClick = onVolumeDown,
        )

        SpacerWidth(width = 8.dp)
        ControlButton(
            imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
            contentDescription = stringResource(Res.string.action_volume_up),
            onClick = onVolumeUp,
        )

        SpacerWidth(width = 24.dp)
        ControlButton(
            imageVector = Icons.Rounded.SkipPrevious,
            contentDescription = stringResource(Res.string.action_previous_channel),
            onClick = onSelectPrevious,
        )

        SpacerWidth(width = 8.dp)
        ControlButton(
            imageVector = Icons.Rounded.SkipNext,
            contentDescription = stringResource(Res.string.action_next_channel),
            onClick = onSelectNext,
        )

        SpacerWidth(width = 24.dp)
        ControlButton(
            imageVector = Icons.AutoMirrored.Rounded.ViewList,
            contentDescription = stringResource(Res.string.action_open_programs),
            onClick = onOpenPrograms,
        )

        SpacerWidth(width = 8.dp)
        ControlButton(
            imageVector = Icons.AutoMirrored.Rounded.FeaturedPlayList,
            contentDescription = stringResource(Res.string.action_open_channels),
            onClick = onOpenChannels,
        )

        SpacerWidth(width = 8.dp)
        ControlButton(
            imageVector = Icons.Rounded.Info,
            contentDescription = stringResource(Res.string.action_open_info),
            onClick = onOpenInfo,
        )

        SpacerWidth(width = 24.dp)
    }
}
