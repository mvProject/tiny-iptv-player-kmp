/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 19:48
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.ui.components.epg.ChannelPrograms
import com.mvproject.tinyiptvkmp.ui.components.indicators.LoadingIndicator
import com.mvproject.tinyiptvkmp.ui.components.overlay.OverlayContent
import com.mvproject.tinyiptvkmp.ui.components.toolbars.AppBarWithSearch
import com.mvproject.tinyiptvkmp.ui.screens.channels.action.TvPlaylistChannelAction
import com.mvproject.tinyiptvkmp.ui.screens.channels.components.ChannelView
import com.mvproject.tinyiptvkmp.ui.screens.channels.components.OverlayChannelOptions
import com.mvproject.tinyiptvkmp.ui.screens.channels.navigation.NavigationGroup
import com.mvproject.tinyiptvkmp.ui.screens.channels.state.TvPlaylistGroupState
import com.mvproject.tinyiptvkmp.ui.theme.dimens
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
                onBackClick = onNavigateBack,
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

        var selectedChannel by remember {
            mutableStateOf(TvPlaylistChannel())
        }

        // todo adaptive size depend on windowSizeClass

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Crossfade(
                targetState = state.viewType
            ) { viewType ->

                ChannelView(
                    modifier = Modifier.fillMaxSize(),
                    viewType = viewType,
                    items = state.channels.filter {
                        it.channelName.contains(searchString, true)
                    },
                    onChannelSelect = { selected ->
                        onNavigateSelected(
                            selected.channelName,
                            state.currentGroup
                        )
                    },
                    onFavoriteClick = { selected ->
                        selectedChannel = selected
                        isChannelOptionOpen.value = true
                    },
                    onShowEpgClick = { selected ->
                        onAction(
                            TvPlaylistChannelAction.ToggleEpgVisibility(
                                selected.channelName,
                                selected.epgId
                            )
                        )
                    },
                )
            }

            LoadingIndicator(isVisible = state.isLoading)

            OverlayContent(
                isVisible = isChannelOptionOpen.value,
                contentAlpha = MaterialTheme.dimens.alpha90,
                onViewTap = { isChannelOptionOpen.value = false },
            ) {
                OverlayChannelOptions(
                    favoriteType = selectedChannel.favoriteType,
                    onToggleFavorite = { favType ->
                        onAction(TvPlaylistChannelAction.ToggleFavourites(selectedChannel, favType))
                        isChannelOptionOpen.value = false
                    },
                )
            }

            OverlayContent(
                isVisible = state.selectedName.isNotBlank(),
                onViewTap = { onAction(TvPlaylistChannelAction.ToggleEpgVisibility()) },
            ) {
                ChannelPrograms(
                    modifier = Modifier
                        .fillMaxHeight(MaterialTheme.dimens.fraction90)
                        .fillMaxWidth(MaterialTheme.dimens.fraction80)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape =
                            RoundedCornerShape(
                                bottomStart = MaterialTheme.dimens.size8,
                                bottomEnd = MaterialTheme.dimens.size8,
                            ),
                        ),
                    title = state.selectedName,
                    programs = state.selectedPrograms,
                )
            }
        }
    }
}
