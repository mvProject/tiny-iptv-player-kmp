/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 20:26
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import com.mvproject.tinyiptvkmp.data.enums.FavoriteType
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.ui.theme.dimens

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChannelCardView(
    modifier: Modifier = Modifier,
    channel: TvPlaylistChannel,
    onChannelSelect: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onShowEpgClick: () -> Unit = {},
) {
    ElevatedCard(
        modifier =
            modifier
                .height(MaterialTheme.dimens.size200)
                .clip(MaterialTheme.shapes.extraSmall)
                .combinedClickable(
                    onClick = onChannelSelect,
                    onLongClick = onShowEpgClick,
                ),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = MaterialTheme.dimens.size8),
            //  contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                ChannelImageLogo(
                    modifier = Modifier.padding(top = MaterialTheme.dimens.size12),
                    channelLogo = channel.channelLogo,
                    channelName = channel.channelName,
                    isLarge = true,
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))

                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = MaterialTheme.dimens.size8),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = channel.channelName,
                        style = MaterialTheme.typography.headlineMedium,
                        color =
                            if (channel.favoriteType != FavoriteType.NONE) {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            } else {
                                MaterialTheme.colorScheme.onPrimary
                            },
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = onFavoriteClick,
            ) {
                Icon(
                    imageVector =
                        if (channel.favoriteType !=
                            FavoriteType.NONE
                        ) {
                            Icons.Rounded.Favorite
                        } else {
                            Icons.Rounded.FavoriteBorder
                        },
                    contentDescription = "PlaybackControl",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
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
