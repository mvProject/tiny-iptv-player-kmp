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
import com.mvproject.tinyiptvkmp.core.common.mvi.CollectUiEffect
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.core.theme.dimens
import com.mvproject.tinyiptvkmp.core.ui.epg.ChannelPrograms
import com.mvproject.tinyiptvkmp.core.ui.indicators.LoadingIndicator
import com.mvproject.tinyiptvkmp.core.ui.overlay.OverlayContent
import com.mvproject.tinyiptvkmp.core.ui.toolbars.AppBarWithSearch
import com.mvproject.tinyiptvkmp.features.channels.components.ChannelView
import com.mvproject.tinyiptvkmp.features.channels.components.OverlayChannelOptions

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

        val isChannelOptionOpen = remember { mutableStateOf(false) }

        var selectedChannel by remember {
            mutableStateOf(TvChannel())
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Crossfade(
                targetState = uiState.viewType
            ) { viewType ->

                val filteredResults = uiState.channels.filter { channel ->
                    channel.channelName.contains(searchString, true)
                }

                ChannelView(
                    modifier = Modifier.fillMaxSize(),
                    viewType = viewType,
                    items = filteredResults,
                    onChannelSelect = { selected ->
                        onAction(
                            GroupChannelsUiAction.SelectChannel(
                                name = selected.channelName,
                                group = uiState.currentGroup
                            )
                        )
                    },
                    onFavoriteClick = { selected ->
                        selectedChannel = selected
                        isChannelOptionOpen.value = true
                    },
                    onShowEpgClick = { selected ->
                        onAction(
                            GroupChannelsUiAction.ToggleEpgVisibility(
                                name = selected.channelName,
                                programId = selected.programId
                            )
                        )
                    },
                )
            }

            LoadingIndicator(isVisible = uiState.isLoading)

            OverlayContent(
                isVisible = isChannelOptionOpen.value,
                contentAlpha = MaterialTheme.dimens.alpha90,
                onViewTap = { isChannelOptionOpen.value = false },
            ) {
                OverlayChannelOptions(
                    favoriteType = selectedChannel.favoriteType,
                    onToggleFavorite = { favType ->
                        onAction(
                            GroupChannelsUiAction.ToggleFavorite(
                                channel = selectedChannel,
                                type = favType
                            )
                        )
                        isChannelOptionOpen.value = false
                    },
                )
            }

            OverlayContent(
                isVisible = uiState.selectedName.isNotBlank(),
                onViewTap = { onAction(GroupChannelsUiAction.ToggleEpgVisibility()) },
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
                    title = uiState.selectedName,
                    programs = uiState.selectedPrograms,
                )
            }
        }
    }
}
