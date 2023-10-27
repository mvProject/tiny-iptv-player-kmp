/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 26.10.23, 15:44
 *
 */

package com.mvproject.tinyiptv.ui.components.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.mvproject.tinyiptv.data.mappers.ListMappers.toActual
import com.mvproject.tinyiptv.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.AppConstants
import com.mvproject.tinyiptv.utils.AppConstants.EMPTY_STRING

@Composable
fun OverlayChannelInfo(
    isFullScreen: Boolean = false,
    currentChannel: TvPlaylistChannel
) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(
                if (isFullScreen)
                    AppConstants.WEIGHT_50
                else AppConstants.WEIGHT_80
            )
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.small
            )
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = RoundedCornerShape(
                        topStart = MaterialTheme.dimens.size8,
                        topEnd = MaterialTheme.dimens.size8
                    )
                )
                .padding(all = MaterialTheme.dimens.size8),
            text = currentChannel.channelName,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        val description =
            currentChannel.channelEpg.toActual().firstOrNull()?.description ?: EMPTY_STRING
        if (description.isEmpty()) {
            // todo fix hardcoded string resources
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = MaterialTheme.dimens.size18,
                        vertical = MaterialTheme.dimens.size48
                    ),
                //   text = stringResource(id = R.string.msg_no_epg_found),
                text = "epg not found",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = MaterialTheme.dimens.size8),
                text = description,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
// todo replace preview
/*
@Composable
@Preview(showBackground = true)
fun DarkPreviewOverlayChannelInfo() {
    VideoAppTheme(darkTheme = true) {
        OverlayChannelInfo(
            currentChannel = TvPlaylistChannel(
                channelName = "Test",
                channelEpg = listOf(
                    EpgProgram(
                        title = "Epg",
                        channelId = "id",
                        start = System.currentTimeMillis() - 30.minutes.inWholeMilliseconds,
                        stop = System.currentTimeMillis() + 30.minutes.inWholeMilliseconds,
                        description = "Epg Description"
                    )
                )
            )
        )
    }
}*/
