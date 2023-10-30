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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import com.mvproject.tinyiptv.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptv.ui.theme.dimens

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChannelCardView(
    modifier: Modifier = Modifier,
    channel: TvPlaylistChannel,
    onChannelSelect: () -> Unit = {},
    onOptionSelect: () -> Unit = {}
) {
    OutlinedCard(
        modifier = modifier
            .heightIn(MaterialTheme.dimens.size200)
            .clip(MaterialTheme.shapes.extraSmall)
            .combinedClickable(
                onClick = onChannelSelect,
                onLongClick = onOptionSelect
            ),

        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {

            ChannelImageLogo(
                channelLogo = channel.channelLogo,
                channelName = channel.channelName,
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size12))

            Text(
                text = channel.channelName,
                style = MaterialTheme.typography.headlineMedium,
                color = if (channel.isInFavorites)
                    MaterialTheme.colorScheme.onSurfaceVariant
                else
                    MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}
// todo replace preview
/*
@Composable
@Preview(showBackground = true)
fun PreviewChannelCardViewFavorite() {
    VideoAppTheme(darkTheme = true) {
        ChannelCardView(channel = PreviewTestData.testProgram.copy(isInFavorites = true))
    }
}*/
