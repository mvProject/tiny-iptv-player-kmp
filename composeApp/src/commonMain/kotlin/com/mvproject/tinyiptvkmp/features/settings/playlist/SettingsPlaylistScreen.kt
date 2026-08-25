/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 13:51
 *
 */

package com.mvproject.tinyiptvkmp.features.settings.playlist

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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.core.base.mvi.CollectUiEffect
import com.mvproject.tinyiptvkmp.core.components.NoItemsView
import com.mvproject.tinyiptvkmp.core.components.adaptive.adaptiveContentWidth
import com.mvproject.tinyiptvkmp.core.components.adaptive.rememberAdaptiveLayoutState
import com.mvproject.tinyiptvkmp.core.components.indicators.LoadingIndicator
import com.mvproject.tinyiptvkmp.core.components.toolbars.AppBarWithBackNav
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.features.settings.components.PlaylistItem
import org.jetbrains.compose.resources.stringResource
import tinyiptvkmp.composeapp.generated.resources.Res
import tinyiptvkmp.composeapp.generated.resources.btn_add_new
import tinyiptvkmp.composeapp.generated.resources.msg_no_items_found
import tinyiptvkmp.composeapp.generated.resources.msg_no_playlist
import tinyiptvkmp.composeapp.generated.resources.scr_playlist_settings_title

@Composable
internal fun SettingsPlaylistScreen(
    viewModel: SettingsPlaylistViewModel,
    onNavigateBack: () -> Unit = {},
    onNavigatePlaylist: (String) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            SettingsPlaylistUiEffect.OnNavigateBack -> onNavigateBack()
            is SettingsPlaylistUiEffect.OnNavigateToPlaylist -> onNavigatePlaylist(effect.id)
        }
    }
    SettingsPlaylistScreen(
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
private fun SettingsPlaylistScreen(
    uiState: SettingsPlaylistUiState,
    onAction: (SettingsPlaylistUiAction) -> Unit,
) {
    val adaptiveLayoutState = rememberAdaptiveLayoutState()

    Scaffold(
        modifier =
        Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars),
        topBar = {
            AppBarWithBackNav(
                appBarTitle = stringResource(Res.string.scr_playlist_settings_title),
                onBackClick = { onAction(SettingsPlaylistUiAction.NavigateBack) },
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
                ElevatedButton(
                    onClick = {
                        onAction(SettingsPlaylistUiAction.NavigateToPlaylist())
                    },
                    modifier = Modifier.adaptiveContentWidth(adaptiveLayoutState),
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
            when (val playlistState = uiState.playlistState) {
                SettingsPlaylistUiState.PlaylistState.Empty -> {
                    NoItemsView(
                        modifier = Modifier.fillMaxSize(),
                        title = stringResource(Res.string.msg_no_items_found),
                        navigateTitle = stringResource(Res.string.msg_no_playlist),
                    )
                }

                is SettingsPlaylistUiState.PlaylistState.Success -> {
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
                                    onAction(SettingsPlaylistUiAction.NavigateToPlaylist(id = item.id))
                                },
                                onDelete = {
                                    onAction(SettingsPlaylistUiAction.DeletePlaylist(playlist = item))
                                },
                            )
                        }
                    }
                }
            }

            LoadingIndicator(isVisible = uiState.isLoading)
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
