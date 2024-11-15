/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 17:36
 *
 */

package com.mvproject.tinyiptvkmp.features.settings.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.core.common.mvi.CollectUiEffect
import com.mvproject.tinyiptvkmp.core.domain.enums.RatioMode
import com.mvproject.tinyiptvkmp.core.domain.enums.ResizeMode
import com.mvproject.tinyiptvkmp.core.theme.dimens
import com.mvproject.tinyiptvkmp.core.ui.toolbars.AppBarWithBackNav
import com.mvproject.tinyiptvkmp.features.settings.components.SettingsSelector
import com.mvproject.tinyiptvkmp.features.settings.player.SettingsPlayerUiState.SettingsPlayer
import org.jetbrains.compose.resources.stringResource
import tinyiptvkmp.composeapp.generated.resources.Res
import tinyiptvkmp.composeapp.generated.resources.option_default_fullscreen_mode
import tinyiptvkmp.composeapp.generated.resources.option_default_ratio_mode
import tinyiptvkmp.composeapp.generated.resources.option_default_resize_mode
import tinyiptvkmp.composeapp.generated.resources.scr_player_settings_title

@Composable
internal fun SettingsPlayerScreen(
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

        Column(
            modifier =
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(MaterialTheme.dimens.size12),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size12)
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
                                .copy(alpha = MaterialTheme.dimens.alpha50),
                            checkedTrackColor = MaterialTheme.colorScheme.onSurfaceVariant,
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
                selectedIndex = uiState.resizeMode,
                isExpanded = uiState.settingsType == SettingsPlayer.ResizeMode,
                options = ResizeMode.entries.map { stringResource(it.title) },
                onClick = {
                    onAction(SettingsPlayerUiAction.ToggleOption(SettingsPlayer.ResizeMode))
                },
                onSelect = {
                    onAction(SettingsPlayerUiAction.SetResizeMode(mode = it))
                }
            )

            SettingsSelector(
                title = stringResource(Res.string.option_default_ratio_mode),
                selectedIndex = uiState.ratioMode,
                isExpanded = uiState.settingsType == SettingsPlayer.RatioMode,
                options = RatioMode.entries.map { stringResource(it.title) },
                onClick = {
                    onAction(SettingsPlayerUiAction.ToggleOption(SettingsPlayer.RatioMode))
                },
                onSelect = {
                    onAction(SettingsPlayerUiAction.SetRatioMode(mode = it))
                }
            )
        }
    }
}

// todo replace preview
/*@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDarkSettingsPlayerView() {
    VideoAppTheme(darkTheme = true) {
        SettingsPlayerView(
            state = SettingsPlayerState()
        )
    }
}*/
