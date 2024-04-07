/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.04.24, 17:09
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.playlist

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import com.mvproject.tinyiptvkmp.data.model.channels.ChannelsGroup
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import com.mvproject.tinyiptvkmp.utils.AppConstants

@Composable
fun PlaylistGroupItemView(
    modifier: Modifier = Modifier,
    group: ChannelsGroup,
) {
    ListItem(
        modifier = modifier,
        leadingContent = {
            Icon(
                modifier =
                    Modifier
                        .size(MaterialTheme.dimens.size42)
                        .clip(MaterialTheme.shapes.small),
                imageVector = Icons.Filled.Folder,
                contentDescription = group.groupName,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        },
        headlineContent = {
            Text(
                text = group.groupName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        },
        trailingContent = {
            if (group.groupContentCount > AppConstants.INT_VALUE_ZERO) {
                Text(
                    modifier = Modifier.width(MaterialTheme.dimens.size78),
                    text = group.groupContentCount.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
                )
            }
        },
    )
}

// todo replace preview
/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDarkPlaylistGroupItemView() {
    VideoAppTheme(darkTheme = true) {
        PlaylistGroupItemView(
            group = ChannelsGroup("testName", groupContentCount = 99)
        )
    }
}*/
