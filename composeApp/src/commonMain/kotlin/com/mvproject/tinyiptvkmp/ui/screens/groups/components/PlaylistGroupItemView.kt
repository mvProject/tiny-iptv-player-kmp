/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 20:26
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.groups.components

import androidx.compose.foundation.clickable
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
import com.mvproject.tinyiptvkmp.data.enums.GroupType
import com.mvproject.tinyiptvkmp.data.model.channels.ChannelsGroup
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import com.mvproject.tinyiptvkmp.utils.AppConstants
import org.jetbrains.compose.resources.stringResource
import tinyiptvkmp.composeapp.generated.resources.Res
import tinyiptvkmp.composeapp.generated.resources.channel_folder_all

@Composable
fun PlaylistGroupItemView(
    modifier: Modifier = Modifier,
    group: ChannelsGroup,
    onSelect: (String, String) -> Unit,
) {
    val title =
        when (group.groupType) {
            GroupType.ALL -> stringResource(resource = Res.string.channel_folder_all)
            GroupType.FAVORITE -> group.groupFavoriteType.name
            GroupType.SPECIFIED -> group.groupName
        }

    ListItem(
        modifier =
            modifier
                .clickable {
                    onSelect(title, group.groupType.name)
                },
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
                text = title,
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
