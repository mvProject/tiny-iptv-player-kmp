/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 20:26
 *
 */

package com.mvproject.tinyiptvkmp.features.groups.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvproject.tinyiptvkmp.core.components.NoItemsView
import com.mvproject.tinyiptvkmp.core.components.adaptive.adaptiveContentWidth
import com.mvproject.tinyiptvkmp.core.components.adaptive.rememberAdaptiveLayoutState
import com.mvproject.tinyiptvkmp.core.components.indicators.LoadingIndicator
import com.mvproject.tinyiptvkmp.core.components.toolbars.AppBarWithSettings
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.app_name
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.msg_no_items_found
import com.mvproject.tinyiptvkmp.core.theme.colorSchemeExtended
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.ChannelsGroup
import com.mvproject.tinyiptvkmp.features.groups.presentation.components.PlaylistGroupItem
import com.mvproject.tinyiptvkmp.features.groups.presentation.components.PlaylistSelector
import com.mvproject.tinyiptvkmp.features.groups.presentation.generated.resources.Res
import com.mvproject.tinyiptvkmp.features.groups.presentation.generated.resources.btn_add_first_playlist
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.Res as DesignSystemRes


@Composable
fun GroupScreen(
    viewModel: GroupViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    GroupScreen(
        state = state,
        onAction = viewModel::onIntent,
    )
}

@Composable
private fun GroupScreen(
    state: GroupState,
    onAction: (GroupAction) -> Unit = {},
) {
    val adaptiveLayoutState = rememberAdaptiveLayoutState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBarWithSettings(
                appBarTitle = stringResource(DesignSystemRes.string.app_name),
                onSettingsClicked = { onAction(GroupAction.NavigateToSettings) },
            )
        },
    ) { paddingValues ->
        Box(
            modifier =
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .adaptiveContentWidth(adaptiveLayoutState)
                        .padding(
                            horizontal = adaptiveLayoutState.contentHorizontalPadding,
                            vertical = MaterialTheme.dimensionSize.size8,
                        ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                GroupUpdateProgress(
                    isUpdating = state.isUpdating,
                    progress = state.progress,
                )

                PlaylistSelector(
                    isVisible = state.isPlaylistSelectorVisible,
                    selectedPlaylistName = state.selectedPlaylist.playlistName,
                    playlists = state.playlists,
                    onPlaylistSelected = { playlistId ->
                        onAction(GroupAction.SelectPlaylist(playlistId))
                    },
                )

                GroupContent(
                    groupState = state.groupState,
                    onAction = onAction,
                )
            }

            LoadingIndicator(isVisible = state.isLoading)
        }
    }
}

@Composable
private fun GroupUpdateProgress(
    isUpdating: Boolean,
    progress: Float,
) {
    AnimatedContent(targetState = isUpdating) { targetIsUpdating ->
        if (targetIsUpdating) {
            LinearProgressIndicator(
                modifier = Modifier
                    .padding(vertical = MaterialTheme.dimensionSize.size8)
                    .fillMaxWidth(),
                progress = { progress },
                trackColor = MaterialTheme.colorScheme.primary,
                color = MaterialTheme.colorSchemeExtended.progress,
                drawStopIndicator = {},
            )
        }
    }
}

@Composable
private fun GroupContent(
    groupState: GroupState.GroupState,
    onAction: (GroupAction) -> Unit,
) {
    when (groupState) {
        GroupState.GroupState.Empty -> EmptyGroups(onAction = onAction)
        is GroupState.GroupState.Success -> GroupsList(
            groups = groupState.groups,
            onAction = onAction,
        )
    }
}

@Composable
private fun EmptyGroups(
    onAction: (GroupAction) -> Unit,
) {
    NoItemsView(
        modifier = Modifier.fillMaxSize(),
        title = stringResource(DesignSystemRes.string.msg_no_items_found),
        navigateTitle = stringResource(Res.string.btn_add_first_playlist),
        onNavigateClick = { onAction(GroupAction.NavigateToSettings) },
    )
}

@Composable
private fun GroupsList(
    groups: ImmutableList<ChannelsGroup>,
    onAction: (GroupAction) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(vertical = MaterialTheme.dimensionSize.size8),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxHeight().clipToBounds(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensionSize.size4),
        ) {
            items(
                items = groups,
                key = { group -> group.groupId },
            ) { item ->
                PlaylistGroupItem(
                    modifier = Modifier.fillMaxWidth().animateItem(),
                    group = item,
                    onUiAction = onAction,
                )
            }
        }
    }
}


// todo replace preview
/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDarkPlaylistDataView() {
    VideoAppTheme(darkTheme = true) {
        GroupView(
            dataState = GroupState(
                groups = testChannelsGroups
            )
        )
    }
}*/
