/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 17:36
 *
 */

package com.mvproject.tinyiptvkmp.features.settings.general

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvproject.tinyiptvkmp.core.base.mvi.CollectUiEffect
import com.mvproject.tinyiptvkmp.core.components.adaptive.adaptiveContentWidth
import com.mvproject.tinyiptvkmp.core.components.adaptive.rememberAdaptiveLayoutState
import com.mvproject.tinyiptvkmp.core.components.buttons.MenuButton
import com.mvproject.tinyiptvkmp.core.components.toolbars.AppBarWithBackNav
import com.mvproject.tinyiptvkmp.core.foundation.common.WEIGHT_1
import com.mvproject.tinyiptvkmp.core.foundation.model.UpdatePeriod
import com.mvproject.tinyiptvkmp.core.mapper.mapToString
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.features.settings.components.SettingsSelector
import com.mvproject.tinyiptvkmp.features.settings.general.SettingsGeneralUiState.SettingsGeneral
import com.mvproject.tinyiptvkmp.features.settings.generated.resources.Res
import com.mvproject.tinyiptvkmp.features.settings.generated.resources.option_update_epg_data
import com.mvproject.tinyiptvkmp.features.settings.generated.resources.option_update_epg_info
import com.mvproject.tinyiptvkmp.features.settings.generated.resources.option_update_title
import com.mvproject.tinyiptvkmp.features.settings.generated.resources.scr_player_settings_title
import com.mvproject.tinyiptvkmp.features.settings.generated.resources.scr_playlist_settings_title
import com.mvproject.tinyiptvkmp.features.settings.generated.resources.scr_settings_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsGeneralScreen(
    viewModel: SettingsGeneralViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToPlayerSettings: () -> Unit,
    onNavigateToPlaylistSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            SettingsGeneralUiEffect.OnNavigateBack -> onNavigateBack()
            SettingsGeneralUiEffect.OnNavigateToPlayerSettings -> onNavigateToPlayerSettings()
            SettingsGeneralUiEffect.OnNavigateToPlaylistSettings -> onNavigateToPlaylistSettings()
        }
    }

    SettingsGeneralScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun SettingsGeneralScreen(
    uiState: SettingsGeneralUiState,
    onAction: (SettingsGeneralUiAction) -> Unit,
) {
    val adaptiveLayoutState = rememberAdaptiveLayoutState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBarWithBackNav(
                appBarTitle = stringResource(Res.string.scr_settings_title),
                onBackClick = { onAction(SettingsGeneralUiAction.NavigateBack) },
            )
        },
    ) { paddingValues ->

        Box(
            modifier = Modifier
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
                            vertical = MaterialTheme.dimensionSize.size8,
                        ),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensionSize.size12),
            ) {
                Column {
                    ListItem(
                        modifier =
                            Modifier
                                .clickable(onClick = { onAction(SettingsGeneralUiAction.NavigateToPlaylistSettings) })
                                .clip(MaterialTheme.shapes.extraSmall),
                        colors =
                            ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.background,
                            ),
                        headlineContent = {
                            Text(
                                text = stringResource(Res.string.scr_playlist_settings_title),
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.titleMedium,
                            )
                        },
                        trailingContent = {
                            MenuButton(
                                imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                                onClick = {
                                    onAction(SettingsGeneralUiAction.NavigateToPlaylistSettings)
                                }
                            )
                        },
                    )
                    HorizontalDivider(
                        modifier =
                            Modifier
                                .padding(horizontal = MaterialTheme.dimensionSize.size8),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }

                Column {
                    ListItem(
                        modifier =
                            Modifier
                                .clickable(onClick = { onAction(SettingsGeneralUiAction.NavigateToPlayerSettings) })
                                .clip(MaterialTheme.shapes.extraSmall),
                        colors =
                            ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.background,
                            ),
                        headlineContent = {
                            Text(
                                text = stringResource(Res.string.scr_player_settings_title),
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.titleMedium,
                            )
                        },
                        trailingContent = {
                            MenuButton(
                                imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                                onClick = {
                                    onAction(SettingsGeneralUiAction.NavigateToPlayerSettings)
                                }
                            )
                        },
                    )

                    HorizontalDivider(
                        modifier =
                            Modifier
                                .padding(horizontal = MaterialTheme.dimensionSize.size8),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.dimensionSize.size8),
                    horizontalArrangement =
                        Arrangement.spacedBy(
                            space = MaterialTheme.dimensionSize.size8,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(WEIGHT_1),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )

                    Text(
                        text = stringResource(Res.string.option_update_title),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    HorizontalDivider(
                        modifier = Modifier.weight(WEIGHT_1),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }

                SettingsSelector(
                    title = stringResource(Res.string.option_update_epg_info),
                    selectedIndex = uiState.infoUpdatePeriod,
                    isExpanded = uiState.settingsType == SettingsGeneral.InfoUpdate,
                    options = UpdatePeriod.entries.map { stringResource(it.mapToString()) },
                    onClick = {
                        onAction(SettingsGeneralUiAction.ToggleOption(SettingsGeneral.InfoUpdate))
                    },
                    onSelect = {
                        onAction(SettingsGeneralUiAction.SetInfoUpdatePeriod(type = it))
                    }
                )

                SettingsSelector(
                    title = stringResource(Res.string.option_update_epg_data),
                    selectedIndex = uiState.epgUpdatePeriod,
                    isExpanded = uiState.settingsType == SettingsGeneral.ProgramsUpdate,
                    options = UpdatePeriod.entries.map { stringResource(it.mapToString()) },
                    onClick = {
                        onAction(SettingsGeneralUiAction.ToggleOption(SettingsGeneral.ProgramsUpdate))
                    },
                    onSelect = {
                        onAction(SettingsGeneralUiAction.SetEpgUpdatePeriod(type = it))
                    }
                )
            }
        }
    }
}
// todo replace preview
/*

@Preview
@Composable
fun PreviewDarkSettingsView() {
    VideoAppTheme(darkTheme = true) {
        SettingsGeneralScreen(state = UiState(), onAction = {})
    }
}
*/
