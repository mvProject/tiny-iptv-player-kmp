/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.playlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import com.mvproject.tinyiptvkmp.data.model.channels.ChannelsGroup
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import com.mvproject.tinyiptvkmp.utils.AppConstants

@Composable
fun PlaylistGroupItemView(
    modifier: Modifier = Modifier,
    group: ChannelsGroup
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(MaterialTheme.dimens.size42)
                .clip(MaterialTheme.shapes.small),
            imageVector = Icons.Filled.Folder,
            contentDescription = group.groupName,
            tint = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

        Text(
            modifier = Modifier.weight(MaterialTheme.dimens.weight6),
            text = group.groupName,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary,
        )

        if (group.groupContentCount > AppConstants.INT_VALUE_ZERO) {
            Text(
                modifier = Modifier.width(MaterialTheme.dimens.size78),
                text = group.groupContentCount.toString(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            )
        }

    }
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
