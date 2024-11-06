/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 20:26
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.groups

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
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvproject.tinyiptvkmp.core.ui.components.indicators.LoadingIndicator
import com.mvproject.tinyiptvkmp.core.ui.components.modifiers.SpacerHeight
import com.mvproject.tinyiptvkmp.core.ui.components.selectors.OptionSelector
import com.mvproject.tinyiptvkmp.core.ui.components.toolbars.AppBarWithSettings
import com.mvproject.tinyiptvkmp.core.ui.components.views.NoItemsView
import com.mvproject.tinyiptvkmp.core.ui.theme.dimens
import com.mvproject.tinyiptvkmp.ui.screens.groups.action.GroupAction
import com.mvproject.tinyiptvkmp.ui.screens.groups.components.OptionsDialog
import com.mvproject.tinyiptvkmp.ui.screens.groups.components.PlaylistGroupItemView
import com.mvproject.tinyiptvkmp.ui.screens.groups.state.GroupState
import com.mvproject.tinyiptvkmp.ui.screens.groups.state.GroupUiState
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
    val groupState by viewModel.groupState.collectAsStateWithLifecycle()
    val groupUiState by viewModel.groupUiState.collectAsStateWithLifecycle()

    GroupScreen(
        state = groupState,
        uiState = groupUiState,
        onNavigateToSettings = onNavigateToSettings,
        onNavigateToGroup = onNavigateToGroup,
        onPlaylistAction = viewModel::processAction,
    )
}

@Composable
private fun GroupScreen(
    state: GroupState,
    uiState: GroupUiState,
    onNavigateToSettings: () -> Unit = {},
    onNavigateToGroup: (String, String) -> Unit,
    onPlaylistAction: (GroupAction) -> Unit = {},
) {
    LifecycleResumeEffect(Unit) {
        onPlaylistAction(GroupAction.RefreshPlaylist)

        onPauseOrDispose { }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBarWithSettings(onSettingsClicked = onNavigateToSettings)
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
                if (state.isPlaylistSelectorVisible) {
                    val isSelectPlaylistOpen = remember { mutableStateOf(false) }

                    OptionSelector(
                        modifier = Modifier.fillMaxWidth(),
                        title = stringResource(Res.string.hint_current_playlist),
                        selectedItem = state.selectedPlaylist.playlistName,
                        isExpanded = isSelectPlaylistOpen.value,
                        onClick = {
                            isSelectPlaylistOpen.value = true
                        },
                    )

                    OptionsDialog(
                        isDialogOpen = isSelectPlaylistOpen,
                        title = stringResource(Res.string.hint_current_playlist),
                        playlists = state.playlists,
                        onItemSelected = { item ->
                            isSelectPlaylistOpen.value = false
                            onPlaylistAction(GroupAction.SelectPlaylist(item))
                        },
                    )

                    SpacerHeight(height = MaterialTheme.dimens.size8)
                }

                when (uiState) {
                    GroupUiState.Empty ->
                        NoItemsView(
                            modifier = Modifier.fillMaxSize(),
                            title = stringResource(Res.string.msg_no_items_found),
                            navigateTitle = stringResource(Res.string.btn_add_first_playlist),
                            onNavigateClick = onNavigateToSettings,
                        )

                    GroupUiState.Groups -> {
                        Column(
                            modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(vertical = MaterialTheme.dimens.size8),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            LazyColumn(
                                modifier = Modifier.fillMaxHeight(),
                                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size4),
                            ) {
                                items(
                                    items = state.channelGroups,
                                    key = { grp -> grp.groupId },
                                ) { item ->
                                    PlaylistGroupItemView(
                                        modifier = Modifier.fillMaxWidth(),
                                        group = item,
                                        onSelect = onNavigateToGroup,
                                    )
                                }
                            }
                        }
                    }

                    GroupUiState.Loading -> LoadingIndicator(isVisible = true)
                }
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
