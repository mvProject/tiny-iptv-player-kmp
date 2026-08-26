package com.mvproject.tinyiptvkmp.platform.mediaplayer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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
    // no need yet
}
