/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 13:51
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.settings.playlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.ui.components.toolbars.AppBarWithBackNav
import com.mvproject.tinyiptvkmp.ui.components.views.NoItemsView
import com.mvproject.tinyiptvkmp.ui.screens.settings.components.PlaylistItemView
import com.mvproject.tinyiptvkmp.ui.screens.settings.playlist.action.SettingsPlaylistAction
import com.mvproject.tinyiptvkmp.ui.screens.settings.playlist.state.SettingsPlaylistState
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import com.mvproject.tinyiptvkmp.utils.AppConstants
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import tinyiptvkmp.composeapp.generated.resources.Res
import tinyiptvkmp.composeapp.generated.resources.btn_add_new
import tinyiptvkmp.composeapp.generated.resources.msg_no_items_found
import tinyiptvkmp.composeapp.generated.resources.msg_no_playlist
import tinyiptvkmp.composeapp.generated.resources.scr_playlist_settings_title

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SettingsPlaylistView(
    dataState: SettingsPlaylistState,
    onNavigateBack: () -> Unit = {},
    onNavigatePlaylist: (String) -> Unit = {},
    onPlaylistAction: (SettingsPlaylistAction) -> Unit = {},
) {
    Scaffold(
        modifier =
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.navigationBars),
        topBar = {
            AppBarWithBackNav(
                appBarTitle = stringResource(Res.string.scr_playlist_settings_title),
                onBackClick = onNavigateBack,
            )
        },
        bottomBar = {
            ElevatedButton(
                onClick = {
                    onNavigatePlaylist(AppConstants.EMPTY_STRING)
                },
                modifier =
                    Modifier
                        .padding(MaterialTheme.dimens.size8)
                        .fillMaxWidth(),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSurface,
                    ),
                shape = MaterialTheme.shapes.small,
            ) {
                Text(
                    text = stringResource(Res.string.btn_add_new),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        },
    ) { paddingValues ->
        Box(
            modifier =
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
        ) {
            LazyColumn(
                modifier =
                    Modifier
                        .fillMaxSize(),
                state = rememberLazyListState(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size4),
                contentPadding = PaddingValues(MaterialTheme.dimens.size8),
            ) {
                items(
                    dataState.playlists.items,
                    key = { it.id },
                ) { item ->
                    PlaylistItemView(
                        modifier = Modifier.fillMaxSize(),
                        item = item,
                        onSelect = {
                            onNavigatePlaylist(item.id.toString())
                        },
                        onDelete = {
                            onPlaylistAction(SettingsPlaylistAction.DeletePlaylist(item))
                        },
                    )
                }
            }

            if (dataState.dataIsEmpty) {
                NoItemsView(
                    modifier = Modifier.fillMaxSize(),
                    title = stringResource(Res.string.msg_no_items_found),
                    navigateTitle = stringResource(Res.string.msg_no_playlist),
                )
            }
        }
    }
}

// todo replace preview
/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDarkSettingsPlaylistView() {
    VideoAppTheme(darkTheme = true) {
        SettingsPlaylistView(
            SettingsPlaylistState(
                playlists = testPlaylists
            )
        )
    }
}
*/
