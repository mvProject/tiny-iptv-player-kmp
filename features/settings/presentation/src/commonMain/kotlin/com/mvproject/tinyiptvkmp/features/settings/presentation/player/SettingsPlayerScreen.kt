/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 17:36
 *
 */

package com.mvproject.tinyiptvkmp.features.settings.presentation.player

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvproject.tinyiptvkmp.core.components.adaptive.adaptiveContentWidth
import com.mvproject.tinyiptvkmp.core.components.adaptive.rememberAdaptiveLayoutState
import com.mvproject.tinyiptvkmp.core.components.toolbars.AppBarWithBackNav
import com.mvproject.tinyiptvkmp.core.foundation.model.VideoSize
import com.mvproject.tinyiptvkmp.core.mapper.mapToString
import com.mvproject.tinyiptvkmp.core.theme.AppTheme
import com.mvproject.tinyiptvkmp.core.theme.colorSchemeExtended
import com.mvproject.tinyiptvkmp.core.theme.dimensionOpacity
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.features.settings.presentation.components.SettingsSelector
import com.mvproject.tinyiptvkmp.features.settings.presentation.generated.resources.Res
import com.mvproject.tinyiptvkmp.features.settings.presentation.generated.resources.option_default_fullscreen_mode
import com.mvproject.tinyiptvkmp.features.settings.presentation.generated.resources.option_default_resize_mode
import com.mvproject.tinyiptvkmp.features.settings.presentation.generated.resources.scr_player_settings_title
import com.mvproject.tinyiptvkmp.features.settings.presentation.player.SettingsPlayerState.SettingsPlayer
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsPlayerScreen(
    viewModel: SettingsPlayerViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsPlayerScreen(
        state = state,
        onAction = viewModel::onIntent,
    )
}

@Composable
private fun SettingsPlayerScreen(
    state: SettingsPlayerState,
    onAction: (SettingsPlayerAction) -> Unit,
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
                onBackClick = { onAction(SettingsPlayerAction.NavigateBack) },
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
                            checked = state.isFullscreenEnabled,
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
                                onAction(SettingsPlayerAction.SetFullScreenMode(state = state))
                            },
                        )
                    }
                )

                SettingsSelector(
                    title = stringResource(Res.string.option_default_resize_mode),
                    selectedIndex = state.videoSize,
                    isExpanded = state.settingsType == SettingsPlayer.VideoSize,
                    options = VideoSize.entries.map { stringResource(it.mapToString()) },
                    onClick = {
                        onAction(SettingsPlayerAction.ToggleOption(SettingsPlayer.VideoSize))
                    },
                    onSelect = {
                        onAction(SettingsPlayerAction.SetVideoSize(mode = it))
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
            state = SettingsPlayerState(),
            onAction = {}
        )
    }
}
