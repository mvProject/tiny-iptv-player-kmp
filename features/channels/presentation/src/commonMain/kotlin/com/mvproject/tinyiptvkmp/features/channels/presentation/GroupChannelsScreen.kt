/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 19:48
 *
 */

package com.mvproject.tinyiptvkmp.features.channels.presentation

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvproject.tinyiptvkmp.core.components.adaptive.rememberAdaptiveLayoutState
import com.mvproject.tinyiptvkmp.core.components.channels.ChannelFavoriteSelector
import com.mvproject.tinyiptvkmp.core.components.channels.ChannelPrograms
import com.mvproject.tinyiptvkmp.core.components.indicators.LoadingIndicator
import com.mvproject.tinyiptvkmp.core.components.overlay.OnScreenDisplay
import com.mvproject.tinyiptvkmp.core.components.toolbars.AppBarWithSearch
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.features.channels.presentation.GroupChannelsState.GroupChannelsOSD
import com.mvproject.tinyiptvkmp.features.channels.presentation.components.GroupChannelsContent
import com.mvproject.tinyiptvkmp.features.channels.presentation.components.favoriteOptionsUiModels
import com.mvproject.tinyiptvkmp.features.channels.presentation.components.toChannelItemUiModel
import com.mvproject.tinyiptvkmp.features.channels.presentation.components.toChannelProgramUiModel
import com.mvproject.tinyiptvkmp.features.channels.presentation.generated.resources.Res
import com.mvproject.tinyiptvkmp.features.channels.presentation.generated.resources.hint_msg_search
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
    val channelItems = remember(state.channels) {
        state.channels.map { channel -> channel.toChannelItemUiModel() }
    }
    val channelById = remember(state.channels, channelItems) {
        state.channels.zip(channelItems).associate { (channel, item) -> item.id to channel }
    }
    val selectedPrograms = remember(state.selectedPrograms) {
        state.selectedPrograms.map { program -> program.toChannelProgramUiModel() }
    }

    Scaffold(
        modifier =
        Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.ime),
        topBar = {
            AppBarWithSearch(
                appBarTitle = state.currentGroup,
                searchTextState = state.searchString,
                searchPlaceholderText = stringResource(Res.string.hint_msg_search),
                onBackClick = {
                    onAction(GroupChannelsAction.NavigateBack)
                },
                onViewTypeChange = { type ->
                    onAction(GroupChannelsAction.ViewTypeChange(type))
                },
                onTextChange = { text ->
                    onAction(GroupChannelsAction.SearchTextChange(text))
                },
            )
        },
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            GroupChannelsContent(
                modifier = Modifier.fillMaxSize(),
                viewType = state.viewType,
                items = channelItems,
                gridMinCellWidth = adaptiveLayoutState.channelGridMinCellWidth,
                contentPadding = PaddingValues(
                    horizontal = adaptiveLayoutState.contentHorizontalPadding,
                    vertical = MaterialTheme.dimensionSize.size4,
                ),
                onChannelSelect = { selected ->
                    channelById[selected.id]?.let { channel ->
                        onAction(
                            GroupChannelsAction.SelectChannel(
                                name = channel.channelName,
                                url = channel.channelUrl,
                                group = state.currentGroup
                            )
                        )
                    }
                },
                onFavoriteClick = { selected ->
                    channelById[selected.id]?.let { channel ->
                        onAction(
                            GroupChannelsAction.OpenOsd(
                                GroupChannelsOSD.ChannelFavorites(channel = channel)
                            )
                        )
                    }
                },
                onShowProgramsClick = { selected ->
                    channelById[selected.id]?.let { channel ->
                        onAction(
                            GroupChannelsAction.OpenOsd(
                                GroupChannelsOSD.ChannelPrograms(channel = channel)
                            )
                        )
                    }
                },
                onEndReached = {
                    onAction(GroupChannelsAction.LoadMore)
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
                                options = favoriteOptionsUiModels(osdType.channel.favoriteType),
                                onSelectFavorite = { option ->
                                    onAction(
                                        GroupChannelsAction.ToggleFavorite(
                                            channel = osdType.channel,
                                            type = FavoriteType.valueOf(option.id)
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
                                programs = selectedPrograms,
                            )
                        }
                    }
                }
            }
        }
    }
}
