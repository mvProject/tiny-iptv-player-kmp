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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.mvproject.tinyiptvkmp.data.PreviewTestData
import com.mvproject.tinyiptvkmp.data.enums.FavoriteType
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.ui.components.buttons.FavoriteButton
import com.mvproject.tinyiptvkmp.ui.components.indicators.ProgramProgressIndicator
import com.mvproject.tinyiptvkmp.ui.components.modifiers.SpacerHeight
import com.mvproject.tinyiptvkmp.ui.components.texts.ChannelTitle
import com.mvproject.tinyiptvkmp.ui.components.texts.EmptyProgramTitle
import com.mvproject.tinyiptvkmp.ui.components.texts.ProgramTitle
import com.mvproject.tinyiptvkmp.ui.theme.VideoAppTheme
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import org.jetbrains.compose.ui.tooling.preview.Preview

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
            .combinedClickable(
                onClick = onChannelSelect,
                onLongClick = onShowEpgClick,
            ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Column(
            modifier = modifier
                .combinedClickable(
                    onClick = onChannelSelect,
                    onLongClick = onShowEpgClick,
                )
                .clip(MaterialTheme.shapes.extraSmall)
        ) {
            Row(
                modifier = Modifier.padding(MaterialTheme.dimens.size8),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size4)
            ) {
                ChannelLogo(
                    channelLogo = channel.channelLogo,
                    channelName = channel.channelName,
                )
                ChannelTitle(
                    modifier = Modifier.weight(MaterialTheme.dimens.weight1),
                    title = channel.channelName,
                    isFavorite = channel.favoriteType != FavoriteType.NONE,
                    lines = 2
                )
                FavoriteButton(
                    isFavorite = channel.favoriteType != FavoriteType.NONE,
                    onClick = onFavoriteClick
                )
            }

            SpacerHeight(MaterialTheme.dimens.weight1)

            if (channel.programs.isEmpty()) {
                EmptyProgramTitle(
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimens.size8)
                )
            } else {
                ProgramTitle(
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimens.size8),
                    title = channel.programs.first().title
                )
            }

            SpacerHeight(MaterialTheme.dimens.weight1)

            if (channel.programs.isNotEmpty()) {
                ProgramProgressIndicator(progress = channel.programs.first().programProgress)
            }
        }
    }
}

@Preview
@Composable
private fun PreviewChannelGridViewFavorite() {
    VideoAppTheme {
        ChannelGridView(channel = PreviewTestData.testProgram)
    }
}
