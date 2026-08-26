package com.mvproject.tinyiptvkmp.platform.mediaplayer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.core.components.buttons.ControlButton

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
    ControlButton(
        imageVector = Icons.Rounded.Close,
        onClick = onNavigateBack,
    )
}
