package com.mvproject.tinyiptvkmp.features.groups.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.core.theme.colorSchemeExtended
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.core.theme.dimensionText
import com.mvproject.tinyiptvkmp.features.groups.GroupUiAction
import com.mvproject.tinyiptvkmp.features.groups.GroupUiState
import org.jetbrains.compose.resources.stringResource
import tinyiptvkmp.composeapp.generated.resources.Res
import tinyiptvkmp.composeapp.generated.resources.hint_current_playlist

@Composable
fun PlaylistSelector(
    uiState: GroupUiState,
    onAction: (GroupUiAction) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth().border(
            width = MaterialTheme.dimensionSize.size1,
            color = MaterialTheme.colorScheme.onSurface,
            shape = MaterialTheme.shapes.extraSmall
        )
    ) {
        if (uiState.isPlaylistSelectorVisible) {
            var isSelectPlaylistOpen by remember { mutableStateOf(false) }

            ListItem(
                modifier = Modifier.clickable {
                    isSelectPlaylistOpen = !isSelectPlaylistOpen
                },
                overlineContent = {
                    Text(
                        text = stringResource(Res.string.hint_current_playlist),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = MaterialTheme.dimensionText.font10,
                    )
                },
                headlineContent = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = uiState.selectedPlaylist.playlistName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            )

            AnimatedContent(
                targetState = isSelectPlaylistOpen,
                transitionSpec = ({
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Down) togetherWith slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up
                    )
                })
            ) { isOpen ->
                if (isOpen) {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        itemsIndexed(uiState.playlists) { index, item ->
                            TextButton(
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.small,
                                contentPadding = PaddingValues(),
                                onClick = {
                                    isSelectPlaylistOpen = false
                                    onAction(GroupUiAction.SelectPlaylist(item))
                                },
                            ) {
                                Text(
                                    text = item.playlistName,
                                    style = MaterialTheme.typography.titleSmall,
                                    color =
                                    if (item.isSelected) {
                                        MaterialTheme.colorSchemeExtended.activeProgramTitle
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    },
                                )
                            }

                            if (index < uiState.playlists.lastIndex) {
                                HorizontalDivider(
                                    modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = MaterialTheme.dimensionSize.size16),
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}