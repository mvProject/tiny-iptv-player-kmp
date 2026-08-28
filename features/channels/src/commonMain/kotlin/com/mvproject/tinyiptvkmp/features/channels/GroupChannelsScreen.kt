/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 19:48
 *
 */

package com.mvproject.tinyiptvkmp.features.channels

import androidx.compose.animation.Crossfade
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvproject.tinyiptvkmp.core.base.mvi.CollectUiEffect
import com.mvproject.tinyiptvkmp.core.components.adaptive.rememberAdaptiveLayoutState
import com.mvproject.tinyiptvkmp.core.components.indicators.LoadingIndicator
import com.mvproject.tinyiptvkmp.core.components.overlay.OnScreenDisplay
import com.mvproject.tinyiptvkmp.core.components.toolbars.AppBarWithSearch
import com.mvproject.tinyiptvkmp.core.foundation.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.features.channels.GroupChannelsUiState.GroupChannelsOSD
import com.mvproject.tinyiptvkmp.features.channels.components.ChannelFavoriteSelector
import com.mvproject.tinyiptvkmp.features.channels.components.ChannelView
import com.mvproject.tinyiptvkmp.features.channels.generated.resources.Res
import com.mvproject.tinyiptvkmp.features.channels.generated.resources.hint_msg_search
import com.mvproject.tinyiptvkmp.features.epg.ChannelPrograms
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun GroupChannelsScreen(
    viewModel: GroupChannelsViewModel,
    onNavigateToPlayer: (String, String, String) -> Unit,
    onNavigateBack: () -> Unit,
) {
    LifecycleResumeEffect(Unit) {
        viewModel.loadChannelsByGroups()

        onPauseOrDispose { }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            GroupChannelsUiEffect.OnNavigateBack -> onNavigateBack()
            is GroupChannelsUiEffect.OnNavigateToPlayer ->
                onNavigateToPlayer(effect.name, effect.group, effect.groupType)
        }
    }

    GroupChannelsScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun GroupChannelsScreen(
    uiState: GroupChannelsUiState,
    onAction: (GroupChannelsUiAction) -> Unit,
) {
    val adaptiveLayoutState = rememberAdaptiveLayoutState()
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
                appBarTitle = uiState.currentGroup,
                searchTextState = searchString,
                searchPlaceholderText = stringResource(Res.string.hint_msg_search),
                onBackClick = {
                    onAction(GroupChannelsUiAction.NavigateBack)
                },
                onViewTypeChange = { type ->
                    onAction(GroupChannelsUiAction.ViewTypeChange(type))
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
            Crossfade(
                targetState = uiState.viewType
            ) { viewType ->

                val filteredResults by remember(uiState.channels, searchString) {
                    derivedStateOf {
                        uiState.channels.filter { channel ->
                            channel.channelName.contains(searchString, true)
                        }
                    }
                }

                ChannelView(
                    modifier = Modifier.fillMaxSize(),
                    viewType = viewType,
                    items = filteredResults,
                    gridMinCellWidth = adaptiveLayoutState.channelGridMinCellWidth,
                    contentPadding = PaddingValues(
                        horizontal = adaptiveLayoutState.contentHorizontalPadding,
                        vertical = MaterialTheme.dimensionSize.size4,
                    ),
                    onChannelSelect = { selected ->
                        onAction(
                            GroupChannelsUiAction.SelectChannel(
                                name = selected.channelName,
                                group = uiState.currentGroup
                            )
                        )
                    },
                    onFavoriteClick = { selected ->
                        onAction(
                            GroupChannelsUiAction.OpenOsd(
                                GroupChannelsOSD.ChannelFavorites(channel = selected)
                            )
                        )
                    },
                    onShowProgramsClick = { selected ->
                        onAction(
                            GroupChannelsUiAction.OpenOsd(
                                GroupChannelsOSD.ChannelPrograms(channel = selected)
                            )
                        )
                    },
                )
            }

            LoadingIndicator(isVisible = uiState.isLoading)

            OnScreenDisplay(
                isVisible = uiState.osdType != null,
                onViewTap = { onAction(GroupChannelsUiAction.CloseOsd) },
            ) {
                uiState.osdType?.let { osdType ->
                    when (osdType) {
                        is GroupChannelsOSD.ChannelFavorites -> {
                            ChannelFavoriteSelector(
                                favoriteType = osdType.channel.favoriteType,
                                onSelectFavorite = { favType ->
                                    onAction(
                                        GroupChannelsUiAction.ToggleFavorite(
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
                                title = uiState.selectedName,
                                programs = uiState.selectedPrograms,
                            )
                        }
                    }
                }
            }
        }
    }
}
