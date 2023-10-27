/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 26.10.23, 15:44
 *
 */

package com.mvproject.tinyiptv.ui.components.channels

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.mvproject.tinyiptv.data.mappers.ListMappers.toActual
import com.mvproject.tinyiptv.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptv.ui.components.epg.ScheduleEpgItemView
import com.mvproject.tinyiptv.ui.theme.dimens

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChannelGridView(
    modifier: Modifier = Modifier,
    channel: TvPlaylistChannel,
    onChannelSelect: () -> Unit = {},
    onOptionSelect: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .heightIn(MaterialTheme.dimens.size140)
            .clip(MaterialTheme.shapes.extraSmall)
            .combinedClickable(
                onClick = onChannelSelect,
                onLongClick = onOptionSelect
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = modifier
                    .padding(MaterialTheme.dimens.size8),
                verticalAlignment = Alignment.CenterVertically,
            ) {
// todo image fix

                /*                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(channel.channelLogo)
                                        .crossfade(true)
                                        .placeholder(R.drawable.no_channel_logo)
                                        .error(R.drawable.no_channel_logo)
                                        .build(),
                                    contentDescription = channel.channelName,
                                    modifier = Modifier
                                        .size(MaterialTheme.dimens.size42)
                                        .clip(MaterialTheme.shapes.small)
                                )*/

                Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

                Text(
                    text = channel.channelName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (channel.isInFavorites)
                        MaterialTheme.colorScheme.onSurfaceVariant
                    else
                        MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .weight(MaterialTheme.dimens.weight5)
                )
            }
            // todo epg count view
            Row(
                modifier = modifier
                    .padding(horizontal = MaterialTheme.dimens.size8),
                verticalAlignment = Alignment.Top
            ) {
                channel.channelEpg.toActual().take(1).forEach {
                    ScheduleEpgItemView(
                        modifier = Modifier
                            .padding(start = MaterialTheme.dimens.size4),
                        program = it
                    )
                }

                if (channel.channelEpg.isEmpty()) {
                    // todo fix hardcoded string resources
                    Text(
                        // text = stringResource(id = R.string.msg_no_epg_found),
                        text = "epg not found",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = MaterialTheme.dimens.size4),
                    )
                }
            }
        }
    }
}

// todo replace preview
/*
@Composable
@Preview(showBackground = true)
fun PreviewChannelGridViewFavorite() {
    VideoAppTheme(darkTheme = true) {
        ChannelGridView(channel = testProgram.copy(isInFavorites = true))
    }
}*/
