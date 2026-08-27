/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 17:36
 *
 */

package com.mvproject.tinyiptvkmp.features.settings.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptvkmp.core.base.mvi.CollectUiEffect
import com.mvproject.tinyiptvkmp.core.components.adaptive.adaptiveContentWidth
import com.mvproject.tinyiptvkmp.core.components.adaptive.rememberAdaptiveLayoutState
import com.mvproject.tinyiptvkmp.core.components.toolbars.AppBarWithBackNav
import com.mvproject.tinyiptvkmp.core.foundation.model.VideoSize
import com.mvproject.tinyiptvkmp.core.mapper.mapToString
import com.mvproject.tinyiptvkmp.core.theme.AppTheme
import com.mvproject.tinyiptvkmp.core.theme.colorSchemeExtended
import com.mvproject.tinyiptvkmp.core.theme.dimensionOpacity
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.features.settings.components.SettingsSelector
import com.mvproject.tinyiptvkmp.features.settings.generated.resources.Res
import com.mvproject.tinyiptvkmp.features.settings.generated.resources.option_default_fullscreen_mode
import com.mvproject.tinyiptvkmp.features.settings.generated.resources.option_default_resize_mode
import com.mvproject.tinyiptvkmp.features.settings.generated.resources.scr_player_settings_title
import com.mvproject.tinyiptvkmp.features.settings.player.SettingsPlayerUiState.SettingsPlayer
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsPlayerScreen(
    viewModel: SettingsPlayerViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            SettingsPlayerUiEffect.OnNavigateBack -> onNavigateBack()
        }
    }
    SettingsPlayerScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun SettingsPlayerScreen(
    uiState: SettingsPlayerUiState,
    onAction: (SettingsPlayerUiAction) -> Unit,
) {
    val adaptiveLayoutState = rememberAdaptiveLayoutState()

    Scaffold(
        modifier =
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.navigationBars),
        topBar = {
            AppBarWithBackNav(
                appBarTitle = stringResource(Res.string.scr_player_settings_title),
                onBackClick = { onAction(SettingsPlayerUiAction.NavigateBack) },
            )
        },
    ) { paddingValues ->

        Box(
            modifier =
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .adaptiveContentWidth(adaptiveLayoutState)
                        .padding(
                            horizontal = adaptiveLayoutState.contentHorizontalPadding,
                            vertical = MaterialTheme.dimensionSize.size12,
                        ),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensionSize.size12)
            ) {
                ListItem(
                    headlineContent = {
                        Text(
                            text = stringResource(Res.string.option_default_fullscreen_mode),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    },
                    trailingContent = {
                        Switch(
                            checked = uiState.isFullscreenEnabled,
                            colors =
                                SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                                    uncheckedThumbColor =
                                        MaterialTheme.colorScheme.primary
                                            .copy(alpha = MaterialTheme.dimensionOpacity.opacity50),
                                    checkedTrackColor = MaterialTheme.colorSchemeExtended.activeInput,
                                    uncheckedTrackColor = MaterialTheme.colorScheme.onSurface,
                                ),
                            onCheckedChange = { state ->
                                onAction(SettingsPlayerUiAction.SetFullScreenMode(state = state))
                            },
                        )
                    }
                )

                SettingsSelector(
                    title = stringResource(Res.string.option_default_resize_mode),
                    selectedIndex = uiState.videoSize,
                    isExpanded = uiState.settingsType == SettingsPlayer.VideoSize,
                    options = VideoSize.entries.map { stringResource(it.mapToString()) },
                    onClick = {
                        onAction(SettingsPlayerUiAction.ToggleOption(SettingsPlayer.VideoSize))
                    },
                    onSelect = {
                        onAction(SettingsPlayerUiAction.SetVideoSize(mode = it))
                    }
                )
            }
        }
    }
}

// todo replace preview
@Preview
@Composable
fun PreviewDarkSettingsPlayerView() {
    AppTheme {
        SettingsPlayerScreen(
            uiState = SettingsPlayerUiState(),
            onAction = {}
        )
    }
}
