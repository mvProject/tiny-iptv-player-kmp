/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 28.10.23, 16:47
 *
 */

package com.mvproject.tinyiptv.ui.screens.playlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mvproject.tinyiptv.MainRes
import com.mvproject.tinyiptv.data.enums.UpdatePeriod
import com.mvproject.tinyiptv.platform.LocalFileSelectButton
import com.mvproject.tinyiptv.ui.components.dialogs.OptionsDialog
import com.mvproject.tinyiptv.ui.components.selectors.OptionSelector
import com.mvproject.tinyiptv.ui.components.toolbars.AppBarWithBackNav
import com.mvproject.tinyiptv.ui.components.views.LoadingView
import com.mvproject.tinyiptv.ui.screens.playlist.action.PlaylistAction
import com.mvproject.tinyiptv.ui.screens.playlist.state.PlaylistState
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.AppConstants.WEIGHT_1

@Composable
fun PlaylistView(
    state: PlaylistState,
    onNavigateBack: () -> Unit = {},
    onPlaylistAction: (PlaylistAction) -> Unit = {}
) {

    LaunchedEffect(state.isComplete) {
        if (state.isComplete) {
            onNavigateBack()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars),
        topBar = {
            AppBarWithBackNav(
                appBarTitle = MainRes.string.pl_msg_playlist_details,
                onBackClick = onNavigateBack,
            )
        },
        bottomBar = {
            ElevatedButton(
                enabled = state.isReadyToSave,
                onClick = {
                    if (state.isEdit) {
                        onPlaylistAction(PlaylistAction.UpdatePlaylist)
                    } else {
                        onPlaylistAction(PlaylistAction.SavePlaylist)
                    }
                },
                modifier = Modifier
                    .padding(MaterialTheme.dimens.size8)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = MaterialTheme.shapes.small
            ) {
                val text = if (state.isEdit) {
                    MainRes.string.pl_btn_update
                } else {
                    MainRes.string.pl_btn_save
                }

                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val isUpdateOptionOpen = remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.dimens.size12),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.listName,
                    onValueChange = {
                        onPlaylistAction(PlaylistAction.SetTitle(it))
                    },
                    placeholder = {
                        Text(
                            text = MainRes.string.pl_hint_name,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    textStyle = MaterialTheme.typography.labelMedium,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLocal,
                    value = if (state.isLocal) state.localName else state.url,
                    onValueChange = {
                        onPlaylistAction(PlaylistAction.SetRemoteUrl(it))
                    },
                    placeholder = {
                        Text(
                            text = MainRes.string.pl_hint_address,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    textStyle = MaterialTheme.typography.labelMedium,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.DarkGray,
                        cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))

                OptionSelector(
                    modifier = Modifier.fillMaxWidth(),
                    title = MainRes.string.pl_hint_update_period,
                    enabled = !state.isLocal,
                    selectedItem = UpdatePeriod.entries[state.updatePeriod].title,
                    isExpanded = isUpdateOptionOpen.value,
                    onClick = {
                        isUpdateOptionOpen.value = true
                    }
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size16))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = MaterialTheme.dimens.size8
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(
                        modifier = Modifier.weight(WEIGHT_1),
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = MainRes.string.pl_title_or,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Divider(
                        modifier = Modifier.weight(WEIGHT_1),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size16))

                LocalFileSelectButton(
                    onPlaylistAction = onPlaylistAction
                )
            }

            LoadingView(
                isVisible = state.isSaving,
            )

            OptionsDialog(
                isDialogOpen = isUpdateOptionOpen,
                title = MainRes.string.pl_hint_update_period,
                selectedIndex = state.updatePeriod,
                items = UpdatePeriod.entries.map {
                    it.title
                },
                onItemSelected = { index ->
                    onPlaylistAction(PlaylistAction.SetUpdatePeriod(index))
                    isUpdateOptionOpen.value = false
                }
            )
        }
    }
}

// todo replace preview
/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDarkPlaylistDetailViewContent() {
    VideoAppTheme(darkTheme = true) {
        PlaylistView(
            state = PlaylistState()
        )
    }
}
*/
