/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.03.24, 14:57
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.channels

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.mvproject.tinyiptvkmp.data.mappers.ListMappers.toActual
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.ui.components.epg.ScheduleEpgItemView
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import tinyiptvkmp.common.generated.resources.Res
import tinyiptvkmp.common.generated.resources.msg_no_epg_found

@OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
@Composable
fun ChannelListView(
    modifier: Modifier = Modifier,
    channel: TvPlaylistChannel,
    onChannelSelect: () -> Unit = {},
    onOptionSelect: () -> Unit = {},
) {
    ListItem(
        modifier =
            modifier
                .combinedClickable(
                    onClick = onChannelSelect,
                    onLongClick = onOptionSelect,
                )
                .clip(MaterialTheme.shapes.extraSmall),
        colors =
            ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        leadingContent = {
            ChannelImageLogo(
                channelLogo = channel.channelLogo,
                channelName = channel.channelName,
            )
        },
        headlineContent = {
            Text(
                text = channel.channelName,
                style = MaterialTheme.typography.bodyMedium,
                color =
                    if (channel.isInFavorites) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onPrimary
                    },
            )
        },
        supportingContent = {
            // todo epg count view
            channel.channelEpg.toActual().take(1).forEach {
                ScheduleEpgItemView(program = it)
            }

            if (channel.channelEpg.isEmpty()) {
                Text(
                    text = stringResource(Res.string.msg_no_epg_found),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
        },
    )
}
