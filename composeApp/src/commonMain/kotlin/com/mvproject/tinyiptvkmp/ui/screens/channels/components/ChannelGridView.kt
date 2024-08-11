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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.mvproject.tinyiptvkmp.data.mappers.ListMappers.toActual
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import tinyiptvkmp.composeapp.generated.resources.Res
import tinyiptvkmp.composeapp.generated.resources.msg_no_epg_found

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChannelGridView(
    modifier: Modifier = Modifier,
    channel: TvPlaylistChannel,
    onChannelSelect: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onShowEpgClick: () -> Unit = {},
) {
    ElevatedCard(
        modifier =
            modifier
                .height(MaterialTheme.dimens.size140)
                .clip(MaterialTheme.shapes.extraSmall)
                .combinedClickable(
                    onClick = onChannelSelect,
                    onLongClick = onShowEpgClick,
                ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
        ) {
            Row(
                modifier =
                    Modifier
                        .padding(
                            horizontal = MaterialTheme.dimens.size8,
                            vertical = MaterialTheme.dimens.size4,
                        ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ChannelImageLogo(
                    channelLogo = channel.channelLogo,
                    channelName = channel.channelName,
                )

                Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

                Text(
                    text = channel.channelName,
                    style = MaterialTheme.typography.bodyMedium,
                    color =
                        if (channel.favoriteType != FavoriteType.NONE) {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        } else {
                            MaterialTheme.colorScheme.onPrimary
                        },
                    modifier =
                        Modifier
                            .weight(MaterialTheme.dimens.weight5),
                )

                Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

                IconButton(
                    // modifier = modifier,
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

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size10))

            if (channel.channelEpg.items.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(Res.string.msg_no_epg_found),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline,
                        modifier =
                            Modifier
                                .padding(MaterialTheme.dimens.size8),
                        textAlign = TextAlign.Center,
                    )
                }
            } else {
                // todo epg count view
                channel.channelEpg.items.toActual().take(1).forEach {
                    ScheduleEpgItemView(
                        modifier =
                            Modifier
                                .padding(horizontal = MaterialTheme.dimens.size4),
                        program = it,
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
