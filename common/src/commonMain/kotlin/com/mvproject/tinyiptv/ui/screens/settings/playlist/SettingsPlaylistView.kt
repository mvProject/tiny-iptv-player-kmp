/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 26.10.23, 16:23
 *
 */

package com.mvproject.tinyiptv.ui.screens.settings.playlist

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
import com.mvproject.tinyiptv.MainRes
import com.mvproject.tinyiptv.ui.components.playlist.PlaylistItemView
import com.mvproject.tinyiptv.ui.components.toolbars.AppBarWithBackNav
import com.mvproject.tinyiptv.ui.components.views.NoItemsView
import com.mvproject.tinyiptv.ui.screens.settings.playlist.action.SettingsPlaylistAction
import com.mvproject.tinyiptv.ui.screens.settings.playlist.state.SettingsPlaylistState
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.AppConstants

@Composable
fun SettingsPlaylistView(
    dataState: SettingsPlaylistState,
    onNavigateBack: () -> Unit = {},
    onNavigatePlaylist: (String) -> Unit = {},
    onPlaylistAction: (SettingsPlaylistAction) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars),
        topBar = {
            AppBarWithBackNav(
                appBarTitle = MainRes.string.scr_playlist_settings_title,
                onBackClick = onNavigateBack,
            )
        },
        bottomBar = {
            ElevatedButton(
                onClick = {
                    onNavigatePlaylist(AppConstants.EMPTY_STRING)
                },
                modifier = Modifier
                    .padding(MaterialTheme.dimens.size8)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = MainRes.string.pl_btn_add_new,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                state = rememberLazyListState(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size4),
                contentPadding = PaddingValues(MaterialTheme.dimens.size8)
            ) {
                items(
                    dataState.playlists,
                    key = { it.id }
                ) { item ->
                    PlaylistItemView(
                        modifier = Modifier.fillMaxSize(),
                        item = item,
                        onSelect = {
                            onNavigatePlaylist(item.id.toString())
                        },
                        onDelete = {
                            onPlaylistAction(SettingsPlaylistAction.DeletePlaylist(item))
                        }
                    )
                }
            }

            if (dataState.dataIsEmpty) {
                NoItemsView(
                    modifier = Modifier.fillMaxSize(),
                    title = MainRes.string.msg_no_items_found,
                    navigateTitle = MainRes.string.pl_msg_no_playlist
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
