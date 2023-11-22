/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.ui.components.epg.PlayerEpgContent
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import com.mvproject.tinyiptvkmp.utils.AppConstants

@Composable
fun OverlayEpg(
    isFullScreen: Boolean = false,
    currentChannel: TvPlaylistChannel
) {
    Column(
        modifier = Modifier
            .fillMaxHeight(MaterialTheme.dimens.fraction90)
            .fillMaxWidth(
                if (isFullScreen)
                    AppConstants.WEIGHT_50
                else AppConstants.WEIGHT_80
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

        PlayerEpgContent(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(
                    bottomStart = MaterialTheme.dimens.size8,
                    bottomEnd = MaterialTheme.dimens.size8
                )
            ),
            epgList = currentChannel.channelEpg
        )
    }
}
// todo replace preview
/*@Composable
@Preview(showBackground = true)
fun DarkPreviewOverlayEpg() {
    VideoAppTheme(darkTheme = true) {
        OverlayEpg(
            currentChannel = TvPlaylistChannel(
                channelName = "test",
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
