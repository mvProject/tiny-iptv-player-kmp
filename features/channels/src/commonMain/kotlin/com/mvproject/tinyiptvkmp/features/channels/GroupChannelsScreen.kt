/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 19:48
 *
 */

package com.mvproject.tinyiptvkmp.features.channels

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvproject.tinyiptvkmp.core.components.adaptive.rememberAdaptiveLayoutState
import com.mvproject.tinyiptvkmp.core.components.indicators.LoadingIndicator
import com.mvproject.tinyiptvkmp.core.components.overlay.OnScreenDisplay
import com.mvproject.tinyiptvkmp.core.components.toolbars.AppBarWithSearch
import com.mvproject.tinyiptvkmp.core.foundation.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.features.channels.GroupChannelsState.GroupChannelsOSD
import com.mvproject.tinyiptvkmp.features.channels.components.ChannelFavoriteSelector
import com.mvproject.tinyiptvkmp.features.channels.components.ChannelPrograms
import com.mvproject.tinyiptvkmp.features.channels.components.ChannelView
import com.mvproject.tinyiptvkmp.features.channels.generated.resources.Res
import com.mvproject.tinyiptvkmp.features.channels.generated.resources.hint_msg_search
import org.jetbrains.compose.resources.stringResource

@Composable
fun GroupChannelsScreen(
    viewModel: GroupChannelsViewModel
) {
    LifecycleResumeEffect(Unit) {
        viewModel.loadChannelsByGroups()

        onPauseOrDispose { }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    GroupChannelsScreen(
        state = state,
        onAction = viewModel::onIntent,
    )
}

@Composable
private fun GroupChannelsScreen(
    state: GroupChannelsState,
    onAction: (GroupChannelsAction) -> Unit,
) {
    val adaptiveLayoutState = rememberAdaptiveLayoutState()
    // TODO(performance): Move search state to a single source of truth; GroupChannelsState already has searchString.
    var searchString by remember {
        mutableStateOf(String.empty)
    }
    // TODO(performance): For large playlists, move filtering out of composition or cache a UI-search index.
    val filteredResults = remember(state.channels, searchString) {
        if (searchString.isBlank()) {
            state.channels
        } else {
            state.channels.filter { channel ->
                channel.channelName.contains(searchString, true)
            }
        }
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
                searchPlaceholderText = stringResource(Res.string.hint_msg_search),
                onBackClick = {
                    onAction(GroupChannelsAction.NavigateBack)
                },
                onViewTypeChange = { type ->
                    onAction(GroupChannelsAction.ViewTypeChange(type))
                },
                onTextChange = { text ->
                    searchString = text
                },
            )
        },
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            // TODO(performance): ChannelView remaps the whole list to UI models and an id lookup map; precompute when channel state changes.
            ChannelView(
                modifier = Modifier.fillMaxSize(),
                viewType = state.viewType,
                items = filteredResults,
                gridMinCellWidth = adaptiveLayoutState.channelGridMinCellWidth,
                contentPadding = PaddingValues(
                    horizontal = adaptiveLayoutState.contentHorizontalPadding,
                    vertical = MaterialTheme.dimensionSize.size4,
                ),
                onChannelSelect = { selected ->
                    onAction(
                        GroupChannelsAction.SelectChannel(
                            name = selected.channelName,
                            group = state.currentGroup
                        )
                    )
                },
                onFavoriteClick = { selected ->
                    onAction(
                        GroupChannelsAction.OpenOsd(
                            GroupChannelsOSD.ChannelFavorites(channel = selected)
                        )
                    )
                },
                onShowProgramsClick = { selected ->
                    onAction(
                        GroupChannelsAction.OpenOsd(
                            GroupChannelsOSD.ChannelPrograms(channel = selected)
                        )
                    )
                },
            )

            LoadingIndicator(isVisible = state.isLoading)

            OnScreenDisplay(
                isVisible = state.osdType != null,
                onViewTap = { onAction(GroupChannelsAction.CloseOsd) },
            ) {
                state.osdType?.let { osdType ->
                    when (osdType) {
                        is GroupChannelsOSD.ChannelFavorites -> {
                            ChannelFavoriteSelector(
                                favoriteType = osdType.channel.favoriteType,
                                onSelectFavorite = { favType ->
                                    onAction(
                                        GroupChannelsAction.ToggleFavorite(
                                            channel = osdType.channel,
                                            type = favType
                                        )
                                    )
                                }
                            )
                        }

                        is GroupChannelsOSD.ChannelPrograms -> {
                            ChannelPrograms(
                                modifier = Modifier
                                    .fillMaxHeight(adaptiveLayoutState.overlayHeightFraction)
                                    .fillMaxWidth(adaptiveLayoutState.overlayWidthFraction)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape =
                                        RoundedCornerShape(
                                            bottomStart = MaterialTheme.dimensionSize.size8,
                                            bottomEnd = MaterialTheme.dimensionSize.size8,
                                        ),
                                    ),
                                title = state.selectedName,
                                programs = state.selectedPrograms,
                            )
                        }
                    }
                }
            }
        }
    }
}
