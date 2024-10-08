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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.ui.components.selectors.OptionSelector
import com.mvproject.tinyiptvkmp.ui.components.toolbars.AppBarWithSettings
import com.mvproject.tinyiptvkmp.ui.components.views.LoadingView
import com.mvproject.tinyiptvkmp.ui.components.views.NoItemsView
import com.mvproject.tinyiptvkmp.ui.screens.groups.action.GroupAction
import com.mvproject.tinyiptvkmp.ui.screens.groups.components.OptionsDialog
import com.mvproject.tinyiptvkmp.ui.screens.groups.components.PlaylistGroupItemView
import com.mvproject.tinyiptvkmp.ui.screens.groups.state.GroupState
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import org.jetbrains.compose.resources.stringResource
import tinyiptvkmp.composeapp.generated.resources.Res
import tinyiptvkmp.composeapp.generated.resources.btn_add_first_playlist
import tinyiptvkmp.composeapp.generated.resources.hint_current_playlist
import tinyiptvkmp.composeapp.generated.resources.msg_no_items_found

@Composable
fun GroupView(
    dataState: GroupState,
    onNavigateToSettings: () -> Unit = {},
    onNavigateToGroup: (String, String) -> Unit,
    onPlaylistAction: (GroupAction) -> Unit = {},
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBarWithSettings(
                onSettingsClicked = onNavigateToSettings,
            )
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
                if (dataState.isPlaylistSelectorVisible) {
                    val isSelectPlaylistOpen = remember { mutableStateOf(false) }

                    var selectedIndex by remember {
                        mutableIntStateOf(dataState.playlistSelectedIndex)
                    }

                    OptionSelector(
                        modifier = Modifier.fillMaxWidth(),
                        title = stringResource(Res.string.hint_current_playlist),
                        selectedItem = dataState.playlistNames.items[selectedIndex],
                        isExpanded = isSelectPlaylistOpen.value,
                        onClick = {
                            isSelectPlaylistOpen.value = true
                        },
                    )

                    OptionsDialog(
                        isDialogOpen = isSelectPlaylistOpen,
                        title = stringResource(Res.string.hint_current_playlist),
                        selectedIndex = selectedIndex,
                        items = dataState.playlistNames.items,
                        onItemSelected = { index ->
                            selectedIndex = index
                            isSelectPlaylistOpen.value = false
                            onPlaylistAction(GroupAction.SelectPlaylist(index))
                        },
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))
                }
                /*

                                LazyRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size4),
                                ) {
                                    items(
                                        items = dataState.favorites.items,
                                        key = { grp -> grp.groupFavoriteType.name },
                                    ) { item ->
                                        PlaylistGroupItemView(
                                            modifier = Modifier.fillMaxWidth(),
                                            group = item,
                                            onSelect = onNavigateToGroup,
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))
                 */

                LazyColumn(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size4),
                ) {
                    item {
                        PlaylistGroupItemView(
                            modifier = Modifier.fillMaxWidth(),
                            group = dataState.allGroup,
                            onSelect = onNavigateToGroup,
                        )
                    }

                    items(
                        items = dataState.favorites.items,
                        key = { grp -> grp.groupFavoriteType.name },
                    ) { item ->
                        PlaylistGroupItemView(
                            modifier = Modifier.fillMaxWidth(),
                            group = item,
                            onSelect = onNavigateToGroup,
                        )
                    }

                    items(
                        items = dataState.groups.items,
                        key = { grp -> grp.groupName },
                    ) { item ->
                        PlaylistGroupItemView(
                            modifier = Modifier.fillMaxWidth(),
                            group = item,
                            onSelect = onNavigateToGroup,
                        )
                    }
                }
            }

            LoadingView(
                isVisible = dataState.isLoading,
            )

            if (dataState.dataIsEmpty) {
                NoItemsView(
                    modifier = Modifier.fillMaxSize(),
                    title = stringResource(Res.string.msg_no_items_found),
                    navigateTitle = stringResource(Res.string.btn_add_first_playlist),
                    onNavigateClick = onNavigateToSettings,
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
