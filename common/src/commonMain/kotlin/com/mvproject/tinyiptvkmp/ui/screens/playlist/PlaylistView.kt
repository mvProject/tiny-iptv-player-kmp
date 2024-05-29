/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 17:36
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.playlist

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
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
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
import com.mvproject.tinyiptvkmp.data.enums.UpdatePeriod
import com.mvproject.tinyiptvkmp.platform.LocalFileSelectButton
import com.mvproject.tinyiptvkmp.ui.components.overlay.OverlayContent
import com.mvproject.tinyiptvkmp.ui.components.overlay.OverlayOptionsMenu
import com.mvproject.tinyiptvkmp.ui.components.selectors.OptionSelector
import com.mvproject.tinyiptvkmp.ui.components.toolbars.AppBarWithBackNav
import com.mvproject.tinyiptvkmp.ui.components.views.LoadingView
import com.mvproject.tinyiptvkmp.ui.data.Options
import com.mvproject.tinyiptvkmp.ui.screens.playlist.action.PlaylistAction
import com.mvproject.tinyiptvkmp.ui.screens.playlist.state.PlaylistState
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import com.mvproject.tinyiptvkmp.utils.AppConstants.WEIGHT_1
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import tinyiptvkmp.common.generated.resources.Res
import tinyiptvkmp.common.generated.resources.btn_save
import tinyiptvkmp.common.generated.resources.btn_update
import tinyiptvkmp.common.generated.resources.hint_address
import tinyiptvkmp.common.generated.resources.hint_name
import tinyiptvkmp.common.generated.resources.hint_update_period
import tinyiptvkmp.common.generated.resources.label_or
import tinyiptvkmp.common.generated.resources.msg_playlist_details

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PlaylistView(
    state: PlaylistState,
    onNavigateBack: () -> Unit = {},
    onPlaylistAction: (PlaylistAction) -> Unit = {},
) {
    LaunchedEffect(state.isComplete) {
        if (state.isComplete) {
            onNavigateBack()
        }
    }

    Scaffold(
        modifier =
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.navigationBars),
        topBar = {
            AppBarWithBackNav(
                appBarTitle = stringResource(Res.string.msg_playlist_details),
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
                modifier =
                    Modifier
                        .padding(MaterialTheme.dimens.size8)
                        .fillMaxWidth(),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSurface,
                    ),
                shape = MaterialTheme.shapes.small,
            ) {
                val text =
                    if (state.isEdit) {
                        Res.string.btn_update
                    } else {
                        Res.string.btn_save
                    }

                Text(
                    text = stringResource(text),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        },
    ) { paddingValues ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        ) {
            val isUpdateOptionOpen = remember { mutableStateOf(false) }

            Column(
                modifier =
                    Modifier
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
                            text = stringResource(Res.string.hint_name),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                        )
                    },
                    textStyle = MaterialTheme.typography.labelMedium,
                    colors =
                        TextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            focusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
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
                            text = stringResource(Res.string.hint_address),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                        )
                    },
                    textStyle = MaterialTheme.typography.labelMedium,
                    colors =
                        TextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.DarkGray,
                            cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            focusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))

                OptionSelector(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(Res.string.hint_update_period),
                    enabled = !state.isLocal,
                    selectedItem = stringResource(UpdatePeriod.entries[state.updatePeriod].title),
                    isExpanded = isUpdateOptionOpen.value,
                    onClick = {
                        isUpdateOptionOpen.value = true
                    },
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size16))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(
                            space = MaterialTheme.dimens.size8,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(WEIGHT_1),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )

                    Text(
                        text = stringResource(Res.string.label_or),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    HorizontalDivider(
                        modifier = Modifier.weight(WEIGHT_1),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size16))

                LocalFileSelectButton(
                    onPlaylistAction = onPlaylistAction,
                )
            }

            LoadingView(
                isVisible = state.isSaving,
            )

            OverlayContent(
                isVisible = isUpdateOptionOpen.value,
                contentAlpha = MaterialTheme.dimens.alpha90,
                onViewTap = { isUpdateOptionOpen.value = false },
            ) {
                OverlayOptionsMenu(
                    title = stringResource(Res.string.hint_update_period),
                    selectedIndex = state.updatePeriod,
                    options =
                        Options(
                            items =
                                UpdatePeriod.entries.map {
                                    stringResource(it.title)
                                },
                        ),
                    onItemSelected = { index ->
                        onPlaylistAction(PlaylistAction.SetUpdatePeriod(index))
                        isUpdateOptionOpen.value = false
                    },
                )
            }
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
