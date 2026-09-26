/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 17:36
 *
 */

package com.mvproject.tinyiptvkmp.features.settings.presentation.general

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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.semantics.Role
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvproject.tinyiptvkmp.core.components.adaptive.adaptiveContentWidth
import com.mvproject.tinyiptvkmp.core.components.adaptive.rememberAdaptiveLayoutState
import com.mvproject.tinyiptvkmp.core.components.selectors.OptionSelector
import com.mvproject.tinyiptvkmp.core.components.selectors.optionSetOf
import com.mvproject.tinyiptvkmp.core.components.toolbars.AppBarWithBackNav
import com.mvproject.tinyiptvkmp.core.foundation.common.WEIGHT_1
import com.mvproject.tinyiptvkmp.core.foundation.model.UpdatePeriod
import com.mvproject.tinyiptvkmp.core.mapper.mapToString
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.features.settings.presentation.general.SettingsGeneralState.SettingsGeneral
import com.mvproject.tinyiptvkmp.features.settings.presentation.generated.resources.Res
import com.mvproject.tinyiptvkmp.features.settings.presentation.generated.resources.option_update_epg_data
import com.mvproject.tinyiptvkmp.features.settings.presentation.generated.resources.option_update_epg_info
import com.mvproject.tinyiptvkmp.features.settings.presentation.generated.resources.option_update_title
import com.mvproject.tinyiptvkmp.features.settings.presentation.generated.resources.scr_player_settings_title
import com.mvproject.tinyiptvkmp.features.settings.presentation.generated.resources.scr_playlist_settings_title
import com.mvproject.tinyiptvkmp.features.settings.presentation.generated.resources.scr_settings_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsGeneralScreen(
    viewModel: SettingsGeneralViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsGeneralScreen(
        state = state,
        onAction = viewModel::onIntent,
    )
}

@Composable
private fun SettingsGeneralScreen(
    state: SettingsGeneralState,
    onAction: (SettingsGeneralAction) -> Unit,
) {
    val adaptiveLayoutState = rememberAdaptiveLayoutState()
    val periods = optionSetOf(
        values = UpdatePeriod.entries,
        label = { period -> stringResource(period.mapToString()) },
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBarWithBackNav(
                appBarTitle = stringResource(Res.string.scr_settings_title),
                onBackClick = { onAction(SettingsGeneralAction.NavigateBack) },
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
                SettingsNavigationRow(
                    title = stringResource(Res.string.scr_playlist_settings_title),
                    onClick = { onAction(SettingsGeneralAction.NavigateToPlaylistSettings) },
                )

                SettingsNavigationRow(
                    title = stringResource(Res.string.scr_player_settings_title),
                    onClick = { onAction(SettingsGeneralAction.NavigateToPlayerSettings) },
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(space = MaterialTheme.dimensionSize.size8),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(WEIGHT_1),
                        color = MaterialTheme.colorScheme.outlineVariant,
                    )

                    Text(
                        text = stringResource(Res.string.option_update_title),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    HorizontalDivider(
                        modifier = Modifier.weight(WEIGHT_1),
                        color = MaterialTheme.colorScheme.outlineVariant,
                    )
                }

                OptionSelector(
                    label = stringResource(Res.string.option_update_epg_info),
                    options = periods.values,
                    optionLabel = periods.label,
                    selected = UpdatePeriod.entries.getOrNull(state.infoUpdatePeriod),
                    expanded = state.settingsType == SettingsGeneral.InfoUpdate,
                    onExpandedChange = {
                        onAction(SettingsGeneralAction.ToggleOption(SettingsGeneral.InfoUpdate))
                    },
                    onSelected = { period ->
                        onAction(SettingsGeneralAction.SetInfoUpdatePeriod(type = period.ordinal))
                    }
                )

                OptionSelector(
                    label = stringResource(Res.string.option_update_epg_data),
                    options = periods.values,
                    optionLabel = periods.label,
                    selected = UpdatePeriod.entries.getOrNull(state.epgUpdatePeriod),
                    expanded = state.settingsType == SettingsGeneral.ProgramsUpdate,
                    onExpandedChange = {
                        onAction(SettingsGeneralAction.ToggleOption(SettingsGeneral.ProgramsUpdate))
                    },
                    onSelected = { period ->
                        onAction(SettingsGeneralAction.SetEpgUpdatePeriod(type = period.ordinal))
                    }
                )
            }
        }
    }
}

@Composable
private fun SettingsNavigationRow(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .clickable(role = Role.Button, onClick = onClick),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface,
            headlineColor = MaterialTheme.colorScheme.onSurface,
            trailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                contentDescription = null,
            )
        },
    )
}
