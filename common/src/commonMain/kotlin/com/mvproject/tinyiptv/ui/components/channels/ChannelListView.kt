/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 26.10.23, 15:44
 *
 */

package com.mvproject.tinyiptv.ui.components.channels

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
fun ChannelListView(
    modifier: Modifier = Modifier,
    channel: TvPlaylistChannel,
    onChannelSelect: () -> Unit = {},
    onOptionSelect: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .combinedClickable(
                onClick = onChannelSelect,
                onLongClick = onOptionSelect
            )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MaterialTheme.dimens.size2,
                    vertical = MaterialTheme.dimens.size4
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            ChannelImageLogo(
                channelLogo = channel.channelLogo,
                channelName = channel.channelName
            )

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

            Column(
                Modifier.weight(MaterialTheme.dimens.weight5)
            ) {
                Text(
                    text = channel.channelName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (channel.isInFavorites)
                        MaterialTheme.colorScheme.onSurfaceVariant
                    else
                        MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size4))

                // todo epg count view
                channel.channelEpg.toActual().take(1).forEach {
                    ScheduleEpgItemView(program = it)
                }

                if (channel.channelEpg.isEmpty()) {
                    // todo fix hardcoded string resources
                    Text(
                        // text = stringResource(id = R.string.msg_no_epg_found),
                        text = "epg not found",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }

}

// todo replace preview
/*@Composable
@Preview()
fun DarkPreviewChannelListViewFav() {
    VideoAppTheme(darkTheme = true) {
        ChannelListView(channel = testProgram.copy(isInFavorites = true))
    }
}*/
