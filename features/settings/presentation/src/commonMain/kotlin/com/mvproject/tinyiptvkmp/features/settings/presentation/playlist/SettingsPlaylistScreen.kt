/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 13:51
 *
 */

package com.mvproject.tinyiptvkmp.features.settings.presentation.playlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvproject.tinyiptvkmp.core.components.NoItemsView
import com.mvproject.tinyiptvkmp.core.components.adaptive.adaptiveContentWidth
import com.mvproject.tinyiptvkmp.core.components.adaptive.rememberAdaptiveLayoutState
import com.mvproject.tinyiptvkmp.core.components.buttons.ActionButton
import com.mvproject.tinyiptvkmp.core.components.indicators.LoadingIndicator
import com.mvproject.tinyiptvkmp.core.components.toolbars.AppBarWithBackNav
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.btn_add_new
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.msg_no_items_found
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.msg_no_playlist
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.features.settings.presentation.components.PlaylistItem
import com.mvproject.tinyiptvkmp.features.settings.presentation.generated.resources.scr_playlist_settings_title
import org.jetbrains.compose.resources.stringResource
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.Res as DesignSystemRes
import com.mvproject.tinyiptvkmp.features.settings.presentation.generated.resources.Res as SettingsRes

@Composable
fun SettingsPlaylistScreen(viewModel: SettingsPlaylistViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsPlaylistScreen(
        state = state,
        onAction = viewModel::onIntent
    )
}

@Composable
private fun SettingsPlaylistScreen(
    state: SettingsPlaylistState,
    onAction: (SettingsPlaylistAction) -> Unit,
) {
    val adaptiveLayoutState = rememberAdaptiveLayoutState()

    Scaffold(
        modifier =
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.navigationBars),
        topBar = {
            AppBarWithBackNav(
                appBarTitle = stringResource(SettingsRes.string.scr_playlist_settings_title),
                onBackClick = { onAction(SettingsPlaylistAction.NavigateBack) },
            )
        },
        bottomBar = {
            Box(
                modifier =
                    Modifier
                        .padding(MaterialTheme.dimensionSize.size8)
                        .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                ActionButton(
                    title = stringResource(DesignSystemRes.string.btn_add_new),
                    onClick = {
                        onAction(SettingsPlaylistAction.NavigateToPlaylist())
                    },
                    modifier = Modifier.adaptiveContentWidth(adaptiveLayoutState),
                )
            }
        },
    ) { paddingValues ->
        Box(
            modifier =
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
            contentAlignment = Alignment.TopCenter,
        ) {
            when (val playlistState = state.playlistState) {
                SettingsPlaylistState.PlaylistState.Empty -> {
                    NoItemsView(
                        modifier = Modifier.fillMaxSize(),
                        title = stringResource(DesignSystemRes.string.msg_no_items_found),
                        navigateTitle = stringResource(DesignSystemRes.string.msg_no_playlist),
                    )
                }

                is SettingsPlaylistState.PlaylistState.Success -> {
                    LazyColumn(
                        modifier =
                            Modifier
                                .fillMaxHeight()
                                .adaptiveContentWidth(adaptiveLayoutState),
                        state = rememberLazyListState(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensionSize.size4),
                        contentPadding = PaddingValues(
                            horizontal = adaptiveLayoutState.contentHorizontalPadding,
                            vertical = MaterialTheme.dimensionSize.size8,
                        ),
                    ) {
                        items(
                            playlistState.playlists,
                            key = { it.id },
                        ) { item ->
                            PlaylistItem(
                                modifier = Modifier.fillMaxSize(),
                                item = item,
                                onSelect = {
                                    onAction(SettingsPlaylistAction.NavigateToPlaylist(id = item.id))
                                },
                                onDelete = {
                                    onAction(SettingsPlaylistAction.DeletePlaylist(playlist = item))
                                },
                            )
                        }
                    }
                }
            }

            LoadingIndicator(isVisible = state.isLoading)
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
