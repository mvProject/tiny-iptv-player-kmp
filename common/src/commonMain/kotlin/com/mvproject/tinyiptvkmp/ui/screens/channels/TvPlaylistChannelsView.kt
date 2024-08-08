/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 19:48
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels

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
import com.mvproject.tinyiptvkmp.data.enums.ChannelsViewType
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.platform.ExecuteOnResume
import com.mvproject.tinyiptvkmp.ui.components.overlay.OverlayContent
import com.mvproject.tinyiptvkmp.ui.components.overlay.OverlayEpg
import com.mvproject.tinyiptvkmp.ui.components.toolbars.AppBarWithSearch
import com.mvproject.tinyiptvkmp.ui.components.views.LoadingView
import com.mvproject.tinyiptvkmp.ui.screens.channels.action.TvPlaylistChannelAction
import com.mvproject.tinyiptvkmp.ui.screens.channels.components.ChannelView
import com.mvproject.tinyiptvkmp.ui.screens.channels.components.OverlayChannelOptions
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import com.mvproject.tinyiptvkmp.utils.AppConstants.INT_VALUE_1
import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty

@Composable
fun TvPlaylistChannelsView(
    viewModel: TvPlaylistChannelsViewModel,
    onNavigateSelected: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onAction: (TvPlaylistChannelAction) -> Unit,
    selectedGroup: String,
    selectedGroupType: String,
) {
    ExecuteOnResume {
        viewModel.loadChannelsByGroups(group = selectedGroup, groupType = selectedGroupType)
    }

    val viewState by viewModel.viewState.collectAsState()
    val channelsState by viewModel.channelsState.collectAsState()

    var searchString by remember {
        mutableStateOf(String.empty)
    }

    Scaffold(
        modifier =
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.ime),
        topBar = {
            AppBarWithSearch(
                appBarTitle = viewState.currentGroup,
                searchTextState = searchString,
                searchWidgetState = viewState.isSearching,
                onBackClick = onNavigateBack,
                onSearchTriggered = {
                    onAction(TvPlaylistChannelAction.SearchTriggered)
                },
                onViewTypeChange = { type ->
                    onAction(TvPlaylistChannelAction.ViewTypeChange(type))
                },
                onTextChange = { text ->
                    searchString = text
                },
            )
        },
    ) { paddingValues ->

        val isChannelOptionOpen = remember { mutableStateOf(false) }
        var selected by remember {
            mutableStateOf(TvPlaylistChannel())
        }

        val channelsList by remember(searchString) {
            derivedStateOf {
                if (searchString.length > INT_VALUE_1) {
                    channelsState.items.filter {
                        it.channelName.contains(searchString, true)
                    }
                } else {
                    channelsState.items
                }
            }
        }

        // todo adaptive size depend on windowSizeClass

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        ) {
            val columns =
                when (viewState.viewType) {
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
                contentPadding =
                    PaddingValues(
                        vertical = MaterialTheme.dimens.size4,
                    ),
                content = {
                    items(
                        items = channelsList,
                        key = { chn -> chn.hashCode() },
                    ) { item ->
                        ChannelView(
                            modifier = Modifier.fillMaxSize(),
                            viewType = viewState.viewType,
                            item = item,
                            onChannelSelect = {
                                onNavigateSelected(item.channelName)
                            },
                            onFavoriteClick = {
                                selected = item
                                isChannelOptionOpen.value = true
                            },
                            onShowEpgClick = {
                                onAction(TvPlaylistChannelAction.ToggleEpgVisibility)
                            },
                        )
                    }
                },
            )

            LoadingView(
                isVisible = viewState.isLoading,
            )

            OverlayContent(
                isVisible = isChannelOptionOpen.value,
                contentAlpha = MaterialTheme.dimens.alpha90,
                onViewTap = { isChannelOptionOpen.value = false },
            ) {
                OverlayChannelOptions(
                    favoriteType = selected.favoriteType,
                    onToggleFavorite = { favType ->
                        onAction(TvPlaylistChannelAction.ToggleFavourites(selected, favType))
                        isChannelOptionOpen.value = false
                    },
                )
            }

            OverlayContent(
                isVisible = viewState.isEpgVisible,
                onViewTap = { onAction(TvPlaylistChannelAction.ToggleEpgVisibility) },
            ) {
                OverlayEpg(
                    isFullScreen = false,
                    currentChannel = selected,
                )
            }
        }
    }
}
