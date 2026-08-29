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
            onClick = onNavigateBack,
        )

        SpacerWidth(width = 32.dp)
        ControlButton(
            imageVector = Icons.AutoMirrored.Rounded.VolumeDown,
            onClick = onVolumeDown,
        )

        SpacerWidth(width = 8.dp)
        ControlButton(
            imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
            onClick = onVolumeUp,
        )

        SpacerWidth(width = 24.dp)
        ControlButton(
            imageVector = Icons.Rounded.SkipPrevious,
            onClick = onSelectPrevious,
        )

        SpacerWidth(width = 8.dp)
        ControlButton(
            imageVector = Icons.Rounded.SkipNext,
            onClick = onSelectNext,
        )

        SpacerWidth(width = 24.dp)
        ControlButton(
            imageVector = Icons.AutoMirrored.Rounded.ViewList,
            onClick = onOpenPrograms,
        )

        SpacerWidth(width = 8.dp)
        ControlButton(
            imageVector = Icons.AutoMirrored.Rounded.FeaturedPlayList,
            onClick = onOpenChannels,
        )

        SpacerWidth(width = 8.dp)
        ControlButton(
            imageVector = Icons.Rounded.Info,
            onClick = onOpenInfo,
        )

        SpacerWidth(width = 24.dp)
    }
}
