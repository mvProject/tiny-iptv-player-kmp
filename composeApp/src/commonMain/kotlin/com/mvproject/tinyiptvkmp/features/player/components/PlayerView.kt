package com.mvproject.tinyiptvkmp.features.player.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.features.player.action.UiActions
import com.mvproject.tinyiptvkmp.features.player.state.TvPlayerState

@Composable
expect fun PlayerView(
    modifier: Modifier = Modifier,
    tvPlayerState: TvPlayerState,
    onUiAction: (UiActions) -> Unit = {},
)

