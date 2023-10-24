/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 23.10.23, 18:17
 *
 */

package com.mvproject.tinyiptv.ui.screens.groups

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptv.R
import com.mvproject.tinyiptv.data.PreviewTestData.testChannelsGroups
import com.mvproject.tinyiptv.ui.components.OptionSelector
import com.mvproject.tinyiptv.ui.components.dialogs.OptionsDialog
import com.mvproject.tinyiptv.ui.components.playlist.PlaylistGroupItemView
import com.mvproject.tinyiptv.ui.components.toolbars.AppBarWithSettings
import com.mvproject.tinyiptv.ui.components.views.LoadingView
import com.mvproject.tinyiptv.ui.components.views.NoItemsView
import com.mvproject.tinyiptv.ui.screens.groups.action.GroupAction
import com.mvproject.tinyiptv.ui.screens.groups.state.GroupState
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import com.mvproject.tinyiptv.ui.theme.dimens
import io.github.aakira.napier.Napier

@Composable
fun GroupView(
    dataState: GroupState,
    onNavigateToSettings: () -> Unit = {},
    onNavigateToGroup: (String) -> Unit = {},
    onPlaylistAction: (GroupAction) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBarWithSettings(
                onSettingsClicked = onNavigateToSettings
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.dimens.size8),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (dataState.isPlaylistSelectorVisible) {
                    val isSelectPlaylistOpen = remember { mutableStateOf(false) }

                    var selectedIndex by remember {
                        mutableIntStateOf(dataState.playlistSelectedIndex)
                    }

                    OptionSelector(
                        modifier = Modifier.fillMaxWidth(),
                        title = stringResource(id = R.string.pl_hint_current_playlist),
                        selectedItem = dataState.playlistNames[selectedIndex],
                        isExpanded = isSelectPlaylistOpen.value,
                        onClick = {
                            isSelectPlaylistOpen.value = true
                        }
                    )

                    OptionsDialog(
                        isDialogOpen = isSelectPlaylistOpen,
                        title = stringResource(id = R.string.pl_hint_current_playlist),
                        selectedIndex = selectedIndex,
                        items = dataState.playlistNames,
                        onItemSelected = { index ->
                            Napier.w("testing selected $index")
                            selectedIndex = index
                            isSelectPlaylistOpen.value = false
                            onPlaylistAction(GroupAction.SelectPlaylist(index))
                        }
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))
                }

                LazyColumn(
                    modifier = Modifier.fillMaxHeight(),
                    state = rememberLazyListState(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size4)

                ) {
                    items(
                        dataState.groups,
                        key = { it.groupName }
                    ) { item ->
                        PlaylistGroupItemView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onNavigateToGroup(item.groupName)
                                },
                            group = item
                        )
                    }
                }
            }

            if (dataState.isLoading) {
                LoadingView()
            }

            if (dataState.dataIsEmpty) {
                NoItemsView(
                    modifier = Modifier.fillMaxSize(),
                    navigateTitle = stringResource(id = R.string.pl_btn_add_first_playlist),
                    onNavigateClick = onNavigateToSettings
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPlaylistDataView() {
    VideoAppTheme {
        GroupView(
            dataState = GroupState(
                groups = testChannelsGroups
            )
        )
    }
}

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
}