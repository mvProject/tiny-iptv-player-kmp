/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 14:32
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.data.model.playlist.Playlist
import com.mvproject.tinyiptvkmp.ui.theme.dimens

@Composable
fun PlaylistItemView(
    modifier: Modifier = Modifier,
    item: Playlist,
    onSelect: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        border =
            BorderStroke(
                width = MaterialTheme.dimens.size1,
                color = MaterialTheme.colorScheme.onSurface,
            ),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
    ) {
        ListItem(
            modifier =
                modifier
                    .clickable {
                        onSelect()
                    },
            headlineContent = {
                Text(
                    text = item.playlistTitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            },
            supportingContent = {
                if (item.playlistUrl.isNotEmpty()) {
                    Text(
                        text = item.playlistUrl,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            },
            trailingContent = {
                FilledIconButton(
                    onClick = onDelete,
                    modifier = Modifier.padding(MaterialTheme.dimens.size8),
                    colors =
                        IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.onSurface,
                            contentColor = MaterialTheme.colorScheme.primary,
                        ),
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Playlist Delete",
                    )
                }
            },
        )
    }
}

// todo replace preview
/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDarkPlaylistsItemView() {
    VideoAppTheme(darkTheme = true) {
        PlaylistItemView(
            item = PreviewTestData.testPlaylist
        )
    }
}*/
