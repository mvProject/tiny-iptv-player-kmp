/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 13:53
 *
 */

package com.mvproject.tinyiptvkmp.features.groups.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.mvproject.tinyiptvkmp.core.common.AppConstants.INT_NO_VALUE
import com.mvproject.tinyiptvkmp.core.domain.model.Playlist
import com.mvproject.tinyiptvkmp.core.theme.dimens

@Composable
fun PlaylistSelectDialog(
    modifier: Modifier = Modifier,
    isDialogOpen: MutableState<Boolean>,
    title: String? = null,
    playlists: List<Playlist>,
    onItemSelected: (Playlist) -> Unit = {},
) {
    AnimatedVisibility(
        visible = isDialogOpen.value,
        enter = scaleIn() + fadeIn(),
        exit = scaleOut() + fadeOut(),
    ) {
        Dialog(
            onDismissRequest = { isDialogOpen.value = false },
        ) {
            val listState = rememberLazyListState()

            LaunchedEffect(playlists) {
                val indexSelected = playlists.indexOfFirst { it.isSelected }
                if (indexSelected > INT_NO_VALUE) {
                    listState.scrollToItem(index = indexSelected)
                }
            }

            Surface(
                modifier =
                    modifier
                        .wrapContentSize()
                        .padding(MaterialTheme.dimens.size8),
                shape = MaterialTheme.shapes.medium,
                shadowElevation = MaterialTheme.dimens.size8,
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    state = listState,
                ) {
                    title?.let { text ->
                        item {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color = MaterialTheme.colorScheme.onSurface,
                                        ).padding(MaterialTheme.dimens.size12),
                            ) {
                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.align(Alignment.Center),
                                )
                            }
                        }
                    }

                    itemsIndexed(playlists) { index, item ->
                        TextButton(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.small,
                            contentPadding = PaddingValues(),
                            onClick = { onItemSelected(item) },
                        ) {
                            Text(
                                text = item.playlistName,
                                style = MaterialTheme.typography.titleSmall,
                                color =
                                    if (item.isSelected) {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    },
                            )
                        }

                        if (index < playlists.lastIndex) {
                            HorizontalDivider(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = MaterialTheme.dimens.size16),
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }
            }
        }
    }
}

// todo replace preview
/*
@Preview(showBackground = true)
@Composable
fun PreviewOptionsDialog() {
    VideoAppTheme(darkTheme = true) {
        OptionsDialog(
            isDialogOpen = mutableStateOf(true),
            selectedIndex = 1,
            title = "Title",
            items = listOf("Option1", "Option2", "Option3", "Option4")
        )
    }
}*/
