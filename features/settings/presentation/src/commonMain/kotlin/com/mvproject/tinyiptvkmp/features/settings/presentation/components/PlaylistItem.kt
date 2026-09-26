/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 14:32
 *
 */

package com.mvproject.tinyiptvkmp.features.settings.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.PlaylistType
import com.mvproject.tinyiptvkmp.features.settings.presentation.generated.resources.Res
import com.mvproject.tinyiptvkmp.features.settings.presentation.generated.resources.btn_delete_playlist
import org.jetbrains.compose.resources.stringResource

@Composable
fun PlaylistItem(
    modifier: Modifier = Modifier,
    item: Playlist,
    onSelect: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    ListItem(
        modifier =
            modifier
                .clip(MaterialTheme.shapes.medium)
                .clickable { onSelect() },
        colors =
            ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            ),
        headlineContent = {
            Text(
                text = item.playlistName,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        supportingContent = {
            if (item.playlistType == PlaylistType.REMOTE && item.playlistSource.isNotBlank()) {
                Text(
                    text = item.playlistSource,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        trailingContent = {
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = stringResource(Res.string.btn_delete_playlist),
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        },
    )
}
