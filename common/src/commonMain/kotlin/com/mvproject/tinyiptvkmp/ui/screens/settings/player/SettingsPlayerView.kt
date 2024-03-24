/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.03.24, 10:49
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.settings.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.data.enums.RatioMode
import com.mvproject.tinyiptvkmp.data.enums.ResizeMode
import com.mvproject.tinyiptvkmp.ui.components.overlay.OverlayContent
import com.mvproject.tinyiptvkmp.ui.components.overlay.OverlayOptionsMenu
import com.mvproject.tinyiptvkmp.ui.components.selectors.OptionSelector
import com.mvproject.tinyiptvkmp.ui.components.toolbars.AppBarWithBackNav
import com.mvproject.tinyiptvkmp.ui.screens.settings.player.action.SettingsPlayerAction
import com.mvproject.tinyiptvkmp.ui.screens.settings.player.state.SettingsPlayerState
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import tinyiptvkmp.common.generated.resources.Res
import tinyiptvkmp.common.generated.resources.option_default_fullscreen_mode
import tinyiptvkmp.common.generated.resources.option_default_ratio_mode
import tinyiptvkmp.common.generated.resources.option_default_resize_mode
import tinyiptvkmp.common.generated.resources.scr_player_settings_title

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SettingsPlayerView(
    state: SettingsPlayerState,
    onNavigateBack: () -> Unit = {},
    onSettingsPlayerAction: (SettingsPlayerAction) -> Unit = {},
) {
    Scaffold(
        modifier =
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.navigationBars),
        topBar = {
            AppBarWithBackNav(
                appBarTitle = stringResource(Res.string.scr_player_settings_title),
                onBackClick = onNavigateBack,
            )
        },
    ) { paddingValues ->

        val isSelectResizeModeOpen = remember { mutableStateOf(false) }
        val isSelectRatioModeOpen = remember { mutableStateOf(false) }

        Column(
            modifier =
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(MaterialTheme.dimens.size12),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    modifier =
                        Modifier
                            .weight(MaterialTheme.dimens.weight6)
                            .padding(horizontal = MaterialTheme.dimens.size8),
                    text = stringResource(Res.string.option_default_fullscreen_mode),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )

                Switch(
                    modifier = Modifier.width(MaterialTheme.dimens.size82),
                    checked = state.isFullscreenEnabled,
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
                        onSettingsPlayerAction(SettingsPlayerAction.SetFullScreenMode(state))
                    },
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size12))

            OptionSelector(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(Res.string.option_default_resize_mode),
                selectedItem = stringResource(ResizeMode.entries[state.resizeMode].title),
                isExpanded = isSelectResizeModeOpen.value,
                onClick = {
                    isSelectResizeModeOpen.value = true
                },
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size12))

            OptionSelector(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(Res.string.option_default_ratio_mode),
                selectedItem = stringResource(RatioMode.entries[state.ratioMode].title),
                isExpanded = isSelectRatioModeOpen.value,
                onClick = {
                    isSelectRatioModeOpen.value = true
                },
            )
        }

        OverlayContent(
            isVisible = isSelectResizeModeOpen.value,
            contentAlpha = MaterialTheme.dimens.alpha90,
            onViewTap = { isSelectResizeModeOpen.value = false },
        ) {
            OverlayOptionsMenu(
                title = stringResource(Res.string.option_default_resize_mode),
                selectedIndex = state.resizeMode,
                items = ResizeMode.entries.map { stringResource(it.title) },
                onItemSelected = { index ->
                    onSettingsPlayerAction(SettingsPlayerAction.SetResizeMode(index))
                    isSelectResizeModeOpen.value = false
                },
            )
        }

        OverlayContent(
            isVisible = isSelectRatioModeOpen.value,
            contentAlpha = MaterialTheme.dimens.alpha90,
            onViewTap = { isSelectRatioModeOpen.value = false },
        ) {
            OverlayOptionsMenu(
                title = stringResource(Res.string.option_default_ratio_mode),
                selectedIndex = state.ratioMode,
                items = RatioMode.entries.map { stringResource(it.title) },
                onItemSelected = { index ->
                    onSettingsPlayerAction(SettingsPlayerAction.SetRatioMode(index))
                    isSelectRatioModeOpen.value = false
                },
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
