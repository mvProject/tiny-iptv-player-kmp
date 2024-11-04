/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 19:48
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels

import androidx.compose.animation.Crossfade
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.mvproject.tinyiptvkmp.data.enums.ChannelsViewType
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.ui.components.overlay.OverlayContent
import com.mvproject.tinyiptvkmp.ui.components.overlay.OverlayEpg
import com.mvproject.tinyiptvkmp.ui.components.toolbars.AppBarWithSearch
import com.mvproject.tinyiptvkmp.ui.components.views.LoadingView
import com.mvproject.tinyiptvkmp.ui.screens.channels.action.TvPlaylistChannelAction
import com.mvproject.tinyiptvkmp.ui.screens.channels.components.ChannelView
import com.mvproject.tinyiptvkmp.ui.screens.channels.components.OverlayChannelOptions
import com.mvproject.tinyiptvkmp.ui.screens.channels.navigation.NavigationGroup
import com.mvproject.tinyiptvkmp.ui.screens.channels.state.TvPlaylistGroupState
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import com.mvproject.tinyiptvkmp.utils.AppConstants.INT_VALUE_1
import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty

@Composable
internal fun TvPlaylistChannelsScreen(
    viewModel: TvPlaylistChannelsViewModel,
    onNavigateSelected: NavigationGroup,
    onNavigateBack: () -> Unit,
) {
    LifecycleResumeEffect(Unit) {
        viewModel.loadChannelsByGroups()

        onPauseOrDispose { }
    }

    val groupState by viewModel.groupState.collectAsState()

    TvPlaylistChannelsScreen(
        state = groupState,
        onAction = viewModel::processAction,
        onNavigateBack = onNavigateBack,
        onNavigateSelected = onNavigateSelected
    )
}

@Composable
private fun TvPlaylistChannelsScreen(
    state: TvPlaylistGroupState,
    onNavigateSelected: NavigationGroup,
    onNavigateBack: () -> Unit,
    onAction: (TvPlaylistChannelAction) -> Unit,
) {
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
                appBarTitle = state.currentGroup,
                searchTextState = searchString,
                searchWidgetState = state.isSearching,
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

        // todo adaptive size depend on windowSizeClass

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            val columns = remember(state.viewType) {
                when (state.viewType) {
                    ChannelsViewType.LIST -> GridCells.Fixed(INT_VALUE_1)
                    else -> GridCells.Adaptive(180.dp)
                }
            }

            Crossfade(
                targetState = state.viewType
            ) { viewType ->
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
                            items = state.channels.filter {
                                it.channelName.contains(searchString, true)
                            },
                            key = { chn -> chn.hashCode() },
                        ) { item ->
                            ChannelView(
                                modifier = Modifier.fillMaxSize(),
                                viewType = viewType,
                                item = item,
                                onChannelSelect = {
                                    onNavigateSelected(
                                        item.channelName,
                                        state.currentGroup
                                    )
                                },
                                onFavoriteClick = {
                                    selected = item
                                    isChannelOptionOpen.value = true
                                },
                                onShowEpgClick = {
                                    onAction(
                                        TvPlaylistChannelAction.ToggleEpgVisibility(
                                            item.channelName,
                                            item.epgId
                                        )
                                    )
                                },
                            )
                        }
                    },
                )
            }

            LoadingView(isVisible = state.isLoading)

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
                isVisible = state.selectedName.isNotBlank(),
                onViewTap = { onAction(TvPlaylistChannelAction.ToggleEpgVisibility()) },
            ) {
                OverlayEpg(
                    state.selectedName,
                    state.selectedPrograms
                )
            }
        }
    }
}
