/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 22.11.23, 09:20
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
import com.mvproject.tinyiptvkmp.MainRes
import com.mvproject.tinyiptvkmp.data.enums.RatioMode
import com.mvproject.tinyiptvkmp.data.enums.ResizeMode
import com.mvproject.tinyiptvkmp.ui.components.dialogs.OptionsDialog
import com.mvproject.tinyiptvkmp.ui.components.selectors.OptionSelector
import com.mvproject.tinyiptvkmp.ui.components.toolbars.AppBarWithBackNav
import com.mvproject.tinyiptvkmp.ui.screens.settings.player.action.SettingsPlayerAction
import com.mvproject.tinyiptvkmp.ui.screens.settings.player.state.SettingsPlayerState
import com.mvproject.tinyiptvkmp.ui.theme.dimens

@Composable
fun SettingsPlayerView(
    state: SettingsPlayerState,
    onNavigateBack: () -> Unit = {},
    onSettingsPlayerAction: (SettingsPlayerAction) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars),
        topBar = {
            AppBarWithBackNav(
                appBarTitle = MainRes.string.scr_player_settings_title,
                onBackClick = onNavigateBack,
            )
        }
    ) { paddingValues ->

        val isSelectResizeModeOpen = remember { mutableStateOf(false) }
        val isSelectRatioModeOpen = remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(MaterialTheme.dimens.size12)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size8),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = MainRes.string.option_default_fullscreen_mode,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Switch(
                    checked = state.isFullscreenEnabled,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.primary.copy(
                            alpha = MaterialTheme.dimens.alpha50
                        ),
                        checkedTrackColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        uncheckedTrackColor = MaterialTheme.colorScheme.onSurface
                    ),
                    onCheckedChange = { state ->
                        onSettingsPlayerAction(SettingsPlayerAction.SetFullScreenMode(state))
                    }
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size12))

            OptionSelector(
                modifier = Modifier.fillMaxWidth(),
                title = MainRes.string.option_default_resize_mode,
                selectedItem = ResizeMode.entries[state.resizeMode].title,
                isExpanded = isSelectResizeModeOpen.value,
                onClick = {
                    isSelectResizeModeOpen.value = true
                }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size12))

            OptionSelector(
                modifier = Modifier.fillMaxWidth(),
                title = MainRes.string.option_default_ratio_mode,
                selectedItem = RatioMode.entries[state.ratioMode].title,
                isExpanded = isSelectRatioModeOpen.value,
                onClick = {
                    isSelectRatioModeOpen.value = true
                }
            )

        }

        OptionsDialog(
            isDialogOpen = isSelectResizeModeOpen,
            title = MainRes.string.option_default_resize_mode,
            selectedIndex = state.resizeMode,
            items = ResizeMode.values().map {
                it.title
            },
            onItemSelected = { index ->
                onSettingsPlayerAction(SettingsPlayerAction.SetResizeMode(index))
                isSelectResizeModeOpen.value = false
            }
        )

        OptionsDialog(
            isDialogOpen = isSelectRatioModeOpen,
            title = MainRes.string.option_default_ratio_mode,
            selectedIndex = state.ratioMode,
            items = RatioMode.values().map {
                it.title
            },
            onItemSelected = { index ->
                onSettingsPlayerAction(SettingsPlayerAction.SetRatioMode(index))
                isSelectRatioModeOpen.value = false
            }
        )
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
