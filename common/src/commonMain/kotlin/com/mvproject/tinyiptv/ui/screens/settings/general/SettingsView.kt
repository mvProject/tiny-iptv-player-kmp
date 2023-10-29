/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 27.10.23, 21:23
 *
 */

package com.mvproject.tinyiptv.ui.screens.settings.general

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptv.data.enums.UpdatePeriod
import com.mvproject.tinyiptv.ui.components.dialogs.OptionsDialog
import com.mvproject.tinyiptv.ui.components.selectors.OptionSelector
import com.mvproject.tinyiptv.ui.components.toolbars.AppBarWithBackNav
import com.mvproject.tinyiptv.ui.screens.settings.general.action.SettingsAction
import com.mvproject.tinyiptv.ui.screens.settings.general.state.SettingsState
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.AppConstants.WEIGHT_1

@Composable
fun SettingsView(
    state: SettingsState,
    onSettingsAction: (SettingsAction) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onNavigatePlaylistSettings: () -> Unit = {},
    onNavigatePlayerSettings: () -> Unit = {},

    ) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
// todo fix hardcoded string resources
            AppBarWithBackNav(
                //   appBarTitle = stringResource(id = R.string.scr_settings_title),
                appBarTitle = "Settings",
                onBackClick = onNavigateBack,
            )
        }
    ) { paddingValues ->

        val isSelectInfoUpdateOpen = remember { mutableStateOf(false) }
        val isSelectEpgUpdateOpen = remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(MaterialTheme.dimens.size8),
        ) {

            TextButton(
                onClick = onNavigatePlaylistSettings,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
// todo fix hardcoded string resources
                    // text = stringResource(id = R.string.scr_playlist_settings_title),
                    text = "Playlists Settings",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(WEIGHT_1))

                FilledIconButton(
                    onClick = onNavigatePlaylistSettings,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowRight,
                        contentDescription = "Playlists Settings"
                    )
                }
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.size8),
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size12))

            TextButton(
                onClick = onNavigatePlayerSettings,
                modifier = Modifier.fillMaxWidth()
            ) {
// todo fix hardcoded string resources
                Text(
                    //  text = stringResource(id = R.string.scr_player_settings_title),
                    text = "Player Settings",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(WEIGHT_1))

                FilledIconButton(
                    onClick = onNavigatePlayerSettings,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowRight,
                        contentDescription = "Player Settings"
                    )
                }
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.size8),
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size12))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.size8),
                horizontalArrangement = Arrangement.spacedBy(
                    space = MaterialTheme.dimens.size8
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier.weight(WEIGHT_1),
                    color = MaterialTheme.colorScheme.onPrimary
                )
// todo fix hardcoded string resources
                Text(
                    // text = stringResource(id = R.string.option_update_title),
                    text = "Update",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Divider(
                    modifier = Modifier.weight(WEIGHT_1),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(
                modifier = Modifier.height(MaterialTheme.dimens.size12)
            )
// todo fix hardcoded string resources
            OptionSelector(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.size8),
                //title = stringResource(id = R.string.option_update_epg_info),
                title = "Epg info period",
                // todo fix string resources
                /*      selectedItem = stringResource(
                          id = UpdatePeriod.entries[state.infoUpdatePeriod].title
                      ),*/
                selectedItem = UpdatePeriod.entries[state.infoUpdatePeriod].title,
                isExpanded = isSelectInfoUpdateOpen.value,
                onClick = {
                    isSelectInfoUpdateOpen.value = true
                }
            )

            Spacer(
                modifier = Modifier.height(MaterialTheme.dimens.size12)
            )
// todo fix hardcoded string resources
            OptionSelector(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.size8),
                // title = stringResource(id = R.string.option_update_epg_data),
                title = "Epg data period",
                // todo fix string resources
                /*   selectedItem = stringResource(
                       id = UpdatePeriod.entries[state.epgUpdatePeriod].title
                   ),*/
                selectedItem = UpdatePeriod.entries[state.epgUpdatePeriod].title,
                isExpanded = isSelectEpgUpdateOpen.value,
                onClick = {
                    isSelectEpgUpdateOpen.value = true
                }
            )
        }
// todo fix hardcoded string resources
        OptionsDialog(
            isDialogOpen = isSelectInfoUpdateOpen,
            //   title = stringResource(id = R.string.pl_hint_update_period),
            title = "Update period",
            selectedIndex = state.infoUpdatePeriod,
            // todo fix string resources
            /*       items = UpdatePeriod.entries.map {
                       stringResource(id = it.title)
                   },*/
            items = UpdatePeriod.entries.map {
                it.title
            },
            onItemSelected = { index ->
                onSettingsAction(SettingsAction.SetInfoUpdatePeriod(index))
                isSelectInfoUpdateOpen.value = false
            }
        )
// todo fix hardcoded string resources
        OptionsDialog(
            isDialogOpen = isSelectEpgUpdateOpen,
            // title = stringResource(id = R.string.pl_hint_update_period),
            title = "Update period",
            selectedIndex = state.epgUpdatePeriod,
            // todo fix string resources
            /*    items = UpdatePeriod.entries.map {
                    stringResource(id = it.title)
                },*/
            items = UpdatePeriod.entries.map {
                it.title
            },
            onItemSelected = { index ->
                onSettingsAction(SettingsAction.SetEpgUpdatePeriod(index))
                isSelectEpgUpdateOpen.value = false
            }
        )
    }
}
// todo replace preview
/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDarkSettingsView() {
    VideoAppTheme(darkTheme = true) {
        SettingsView(state = SettingsState())
    }
}*/
