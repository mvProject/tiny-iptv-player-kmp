/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 20:26
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.core.common.PreviewTestData
import com.mvproject.tinyiptvkmp.core.domain.model.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.core.ui.components.buttons.FavoriteButton
import com.mvproject.tinyiptvkmp.core.ui.components.texts.ChannelTitleLarge
import com.mvproject.tinyiptvkmp.core.ui.theme.VideoAppTheme
import com.mvproject.tinyiptvkmp.core.ui.theme.dimens
import com.mvproject.tinyiptvkmp.data.enums.FavoriteType
import org.jetbrains.compose.ui.tooling.preview.Preview

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
            .combinedClickable(
                onClick = onChannelSelect,
                onLongClick = onShowEpgClick,
            ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            ChannelLogo(
                modifier = Modifier
                    .padding(top = MaterialTheme.dimens.size8)
                    .align(Alignment.TopCenter),
                channelLogo = channel.channelLogo,
                channelName = channel.channelName,
                imageSize = MaterialTheme.dimens.size96,
            )

            ChannelTitleLarge(
                modifier = Modifier
                    .padding(bottom = MaterialTheme.dimens.size8)
                    .padding(horizontal = MaterialTheme.dimens.size8)
                    .align(Alignment.BottomCenter),
                title = channel.channelName,
                isFavorite = channel.favoriteType != FavoriteType.NONE
            )

            FavoriteButton(
                modifier = Modifier.align(Alignment.TopEnd),
                isFavorite = channel.favoriteType != FavoriteType.NONE,
                onClick = onFavoriteClick
            )
        }
    }
}

@Composable
@Preview
private fun PreviewChannelCardViewFavorite() {
    VideoAppTheme {
        ChannelCardView(channel = PreviewTestData.testProgram)
    }
}
