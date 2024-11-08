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
import com.mvproject.tinyiptvkmp.core.common.mvi.CollectSideEffect
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.core.theme.dimens
import com.mvproject.tinyiptvkmp.core.ui.epg.ChannelPrograms
import com.mvproject.tinyiptvkmp.core.ui.indicators.LoadingIndicator
import com.mvproject.tinyiptvkmp.core.ui.overlay.OverlayContent
import com.mvproject.tinyiptvkmp.core.ui.toolbars.AppBarWithSearch
import com.mvproject.tinyiptvkmp.features.channels.GroupChannelsContract.UiAction
import com.mvproject.tinyiptvkmp.features.channels.GroupChannelsContract.UiState
import com.mvproject.tinyiptvkmp.features.channels.components.ChannelView
import com.mvproject.tinyiptvkmp.features.channels.components.OverlayChannelOptions

@Composable
internal fun GroupChannelsScreen(
    viewModel: GroupChannelsViewModel,
    onNavigateSelected: (String, String) -> Unit,
    onNavigateBack: () -> Unit,
) {
    LifecycleResumeEffect(Unit) {
        viewModel.loadChannelsByGroups()

        onPauseOrDispose { }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CollectSideEffect(viewModel.uiEffect) {
        when (it) {
            GroupChannelsContract.UiEffect.NavigateBack -> onNavigateBack()
            is GroupChannelsContract.UiEffect.NavigateToSelected ->
                onNavigateSelected(it.name, it.group)
        }
    }

    GroupChannelsScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun GroupChannelsScreen(
    uiState: UiState,
    onAction: (UiAction) -> Unit,
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
                    onAction(UiAction.NavigateBack)
                },
                onViewTypeChange = { type ->
                    onAction(UiAction.ViewTypeChange(type))
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

        // todo adaptive size depend on windowSizeClass

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Crossfade(
                targetState = uiState.viewType
            ) { viewType ->

                ChannelView(
                    modifier = Modifier.fillMaxSize(),
                    viewType = viewType,
                    items = uiState.channels.filter {
                        it.channelName.contains(searchString, true)
                    },
                    onChannelSelect = { selected ->
                        onAction(
                            UiAction.NavigateToSelected(
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
                            UiAction.ToggleEpgVisibility(
                                name = selected.channelName,
                                epgID = selected.epgId
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
                            UiAction.ToggleFavourites(
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
                onViewTap = { onAction(UiAction.ToggleEpgVisibility()) },
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
