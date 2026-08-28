/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 20:26
 *
 */

package com.mvproject.tinyiptvkmp.features.channels.components

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
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptvkmp.PreviewTestData
import com.mvproject.tinyiptvkmp.core.components.buttons.FavoriteButton
import com.mvproject.tinyiptvkmp.core.components.texts.ChannelTitleLarge
import com.mvproject.tinyiptvkmp.core.theme.AppTheme
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.TvChannelWithPrograms
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.FavoriteType

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChannelCardView(
    modifier: Modifier = Modifier,
    channel: TvChannelWithPrograms,
    onChannelSelect: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onShowEpgClick: () -> Unit = {},
) {
    ElevatedCard(
        modifier =
        modifier
            .height(MaterialTheme.dimensionSize.size200)
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
                    .padding(top = MaterialTheme.dimensionSize.size8)
                    .align(Alignment.TopCenter),
                channelLogo = channel.channelLogo,
                channelName = channel.channelName,
                imageSize = MaterialTheme.dimensionSize.size96,
            )

            ChannelTitleLarge(
                modifier = Modifier
                    .padding(bottom = MaterialTheme.dimensionSize.size8)
                    .padding(horizontal = MaterialTheme.dimensionSize.size8)
                    .align(Alignment.BottomCenter),
                title = channel.channelName,
                isFavorite = channel.favoriteType != FavoriteType.NONE.name
            )

            FavoriteButton(
                modifier = Modifier.align(Alignment.TopEnd),
                isFavorite = channel.favoriteType != FavoriteType.NONE.name,
                onClick = onFavoriteClick
            )
        }
    }
}

@Composable
@Preview
private fun PreviewChannelCardViewFavorite() {
    AppTheme {
        ChannelCardView(channel = PreviewTestData.testProgramWithPrograms)
    }
}
