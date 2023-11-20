/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 26.10.23, 15:44
 *
 */

package com.mvproject.tinyiptv.ui.screens.channels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mvproject.tinyiptv.data.enums.ChannelsViewType
import com.mvproject.tinyiptv.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptv.platform.ExecuteOnResume
import com.mvproject.tinyiptv.ui.components.channels.ChannelView
import com.mvproject.tinyiptv.ui.components.dialogs.ChannelOptionsDialog
import com.mvproject.tinyiptv.ui.components.overlay.OverlayContent
import com.mvproject.tinyiptv.ui.components.overlay.OverlayEpg
import com.mvproject.tinyiptv.ui.components.toolbars.AppBarWithSearch
import com.mvproject.tinyiptv.ui.components.views.LoadingView
import com.mvproject.tinyiptv.ui.screens.channels.action.TvPlaylistChannelAction
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.AppConstants.INT_VALUE_1

@Composable
fun TvPlaylistChannelsView(
    viewModel: TvPlaylistChannelsViewModel,
    onNavigateSelected: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onAction: (TvPlaylistChannelAction) -> Unit,
    groupSelected: String
) {
    ExecuteOnResume() {
        viewModel.loadChannelsByGroups(groupSelected)
    }

    val viewState by viewModel.viewState.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.ime),
        topBar = {
            AppBarWithSearch(
                appBarTitle = viewState.currentGroup,
                searchTextState = viewState.searchString,
                searchWidgetState = viewState.isSearching,
                onBackClick = onNavigateBack,
                onSearchTriggered = {
                    onAction(TvPlaylistChannelAction.SearchTriggered)
                },
                onViewTypeChange = { type ->
                    onAction(TvPlaylistChannelAction.ViewTypeChange(type))
                },
                onTextChange = { text ->
                    onAction(TvPlaylistChannelAction.SearchTextChange(text))
                }
            )
        }
    ) { paddingValues ->

        val isChannelOptionOpen = remember { mutableStateOf(false) }
        var selected by remember {
            mutableStateOf(TvPlaylistChannel())
        }

        val channelsList by remember {
            derivedStateOf {
                if (viewState.searchString.length > INT_VALUE_1)
                    viewModel.channels.filter {
                        it.channelName.contains(viewState.searchString, true)
                    }
                else viewModel.channels
            }
        }

        // todo adaptive size depend on windowSizeClass

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val columns = when (viewState.viewType) {
                ChannelsViewType.LIST -> {
                    GridCells.Fixed(INT_VALUE_1)
                }

                else -> {
                    GridCells.Adaptive(180.dp)
                }
            }

            LazyVerticalGrid(
                modifier = Modifier.fillMaxHeight(),
                columns = columns,
                state = rememberLazyGridState(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size8),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size8),
                contentPadding = PaddingValues(
                    vertical = MaterialTheme.dimens.size4
                ),
                content = {
                    items(
                        items = channelsList,
                        key = { it.channelUrl }
                    ) { item ->
                        ChannelView(
                            modifier = Modifier.fillMaxSize(),
                            viewType = viewState.viewType,
                            item = item,
                            onOptionSelect = {
                                selected = item
                                isChannelOptionOpen.value = true
                            },
                            onChannelSelect = {
                                onNavigateSelected(item.channelUrl)
                            }
                        )
                    }
                }
            )

            LoadingView(
                isVisible = viewState.isLoading,
            )

            ChannelOptionsDialog(
                isDialogOpen = isChannelOptionOpen,
                isInFavorite = selected.isInFavorites,
                isEpgEnabled = selected.isEpgUsing,
                isEpgUsing = selected.epgId.isNotEmpty(),
                onToggleFavorite = {
                    onAction(TvPlaylistChannelAction.ToggleFavourites(selected))
                    isChannelOptionOpen.value = false
                },
                onToggleEpgState = {
                    onAction(TvPlaylistChannelAction.ToggleEpgState(selected))
                    isChannelOptionOpen.value = false

                },
                onShowEpg = {
                    onAction(TvPlaylistChannelAction.ToggleEpgVisibility)
                    isChannelOptionOpen.value = false
                }
            )

            OverlayContent(
                isVisible = viewState.isEpgVisible,
                onViewTap = { onAction(TvPlaylistChannelAction.ToggleEpgVisibility) }
            ) {
                OverlayEpg(
                    isFullScreen = false,
                    currentChannel = selected
                )
            }

        }
    }
}