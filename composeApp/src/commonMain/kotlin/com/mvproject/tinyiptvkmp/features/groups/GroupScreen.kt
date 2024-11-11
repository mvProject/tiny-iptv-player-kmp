/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 20:26
 *
 */

package com.mvproject.tinyiptvkmp.features.groups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvproject.tinyiptvkmp.core.common.mvi.CollectUiEffect
import com.mvproject.tinyiptvkmp.core.theme.dimens
import com.mvproject.tinyiptvkmp.core.ui.indicators.LoadingIndicator
import com.mvproject.tinyiptvkmp.core.ui.modifiers.SpacerHeight
import com.mvproject.tinyiptvkmp.core.ui.selectors.OptionSelector
import com.mvproject.tinyiptvkmp.core.ui.toolbars.AppBarWithSettings
import com.mvproject.tinyiptvkmp.core.ui.views.NoItemsView
import com.mvproject.tinyiptvkmp.features.groups.components.PlaylistGroupItem
import com.mvproject.tinyiptvkmp.features.groups.components.PlaylistSelectDialog
import org.jetbrains.compose.resources.stringResource
import tinyiptvkmp.composeapp.generated.resources.Res
import tinyiptvkmp.composeapp.generated.resources.btn_add_first_playlist
import tinyiptvkmp.composeapp.generated.resources.hint_current_playlist
import tinyiptvkmp.composeapp.generated.resources.msg_no_items_found


@Composable
internal fun GroupScreen(
    viewModel: GroupViewModel,
    onNavigateToSettings: () -> Unit = {},
    onNavigateToGroup: (String, String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            is GroupUiEffect.OnNavigateToGroup -> onNavigateToGroup(effect.title, effect.group)
            GroupUiEffect.OnNavigateToSettings -> onNavigateToSettings()
        }
    }

    GroupScreen(
        uiState = uiState,
        onUiAction = viewModel::onAction,
    )
}

@Composable
private fun GroupScreen(
    uiState: GroupUiState,
    onUiAction: (GroupUiAction) -> Unit = {},
) {
    LifecycleResumeEffect(Unit) {
        onUiAction(GroupUiAction.RefreshPlaylist)

        onPauseOrDispose { }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBarWithSettings(onSettingsClicked = { onUiAction(GroupUiAction.NavigateToSettings) })
        },
    ) { paddingValues ->
        Box(
            modifier =
            Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            Column(
                modifier =
                Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.dimens.size8),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                if (uiState.isPlaylistSelectorVisible) {
                    val isSelectPlaylistOpen = remember { mutableStateOf(false) }

                    OptionSelector(
                        modifier = Modifier.fillMaxWidth(),
                        title = stringResource(Res.string.hint_current_playlist),
                        selectedItem = uiState.selectedPlaylist.playlistName,
                        isExpanded = isSelectPlaylistOpen.value,
                        onClick = {
                            isSelectPlaylistOpen.value = true
                        },
                    )

                    PlaylistSelectDialog(
                        isDialogOpen = isSelectPlaylistOpen,
                        title = stringResource(Res.string.hint_current_playlist),
                        playlists = uiState.playlists,
                        onItemSelected = { item ->
                            isSelectPlaylistOpen.value = false
                            onUiAction(GroupUiAction.SelectPlaylist(item))
                        },
                    )

                    SpacerHeight(height = MaterialTheme.dimens.size8)
                }
                when (val groupState = uiState.groupState) {
                    GroupUiState.GroupState.Empty -> NoItemsView(
                        modifier = Modifier.fillMaxSize(),
                        title = stringResource(Res.string.msg_no_items_found),
                        navigateTitle = stringResource(Res.string.btn_add_first_playlist),
                        onNavigateClick = { onUiAction(GroupUiAction.NavigateToSettings) },
                    )

                    is GroupUiState.GroupState.Success -> {
                        Column(
                            modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(vertical = MaterialTheme.dimens.size8),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            LazyColumn(
                                modifier = Modifier.fillMaxHeight().clipToBounds(),
                                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size4),
                            ) {
                                items(
                                    items = groupState.groups,
                                    key = { grp -> grp.groupId },
                                ) { item ->
                                    PlaylistGroupItem(
                                        modifier = Modifier.fillMaxWidth().animateItem(),
                                        group = item,
                                        onUiAction = onUiAction
                                    )
                                }
                            }
                        }
                    }
                }
            }

            LoadingIndicator(isVisible = uiState.isLoading)
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
