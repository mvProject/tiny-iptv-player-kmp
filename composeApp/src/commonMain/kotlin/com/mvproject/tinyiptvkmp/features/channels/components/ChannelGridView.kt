/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 20:26
 *
 */

package com.mvproject.tinyiptvkmp.features.channels.components

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
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptvkmp.core.components.buttons.FavoriteButton
import com.mvproject.tinyiptvkmp.core.components.indicators.ProgramProgressIndicator
import com.mvproject.tinyiptvkmp.core.components.modifiers.SpacerHeight
import com.mvproject.tinyiptvkmp.core.components.texts.ChannelTitle
import com.mvproject.tinyiptvkmp.core.components.texts.EmptyProgramTitle
import com.mvproject.tinyiptvkmp.core.components.texts.ProgramTitle
import com.mvproject.tinyiptvkmp.core.domain.PreviewTestData
import com.mvproject.tinyiptvkmp.core.theme.AppTheme
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.core.theme.dimensionWeight
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.TvChannelWithPrograms
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.FavoriteType
import org.jetbrains.compose.resources.stringResource
import tinyiptvkmp.composeapp.generated.resources.Res
import tinyiptvkmp.composeapp.generated.resources.msg_no_epg_found

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChannelGridView(
    modifier: Modifier = Modifier,
    channel: TvChannelWithPrograms,
    onChannelSelect: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onShowEpgClick: () -> Unit = {},
) {
    ElevatedCard(
        modifier =
        modifier
            .height(MaterialTheme.dimensionSize.size140)
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
                modifier = Modifier.padding(MaterialTheme.dimensionSize.size8),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensionSize.size4)
            ) {
                ChannelLogo(
                    channelLogo = channel.channelLogo,
                    channelName = channel.channelName,
                )
                ChannelTitle(
                    modifier = Modifier.weight(MaterialTheme.dimensionWeight.weight1),
                    title = channel.channelName,
                    isFavorite = channel.favoriteType != FavoriteType.NONE.name,
                    lines = 2
                )
                FavoriteButton(
                    isFavorite = channel.favoriteType != FavoriteType.NONE.name,
                    onClick = onFavoriteClick
                )
            }

            SpacerHeight(MaterialTheme.dimensionWeight.weight1)

            if (channel.programs.isEmpty()) {
                EmptyProgramTitle(
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimensionSize.size8),
                    title = stringResource(Res.string.msg_no_epg_found),
                )
            } else {
                ProgramTitle(
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimensionSize.size8),
                    title = channel.programs.first().title
                )
            }

            SpacerHeight(MaterialTheme.dimensionWeight.weight1)

            if (channel.programs.isNotEmpty()) {
                ProgramProgressIndicator(progress = channel.programs.first().programProgress)
            }
        }
    }
}

@Preview
@Composable
private fun PreviewChannelGridViewFavorite() {
    AppTheme {
        ChannelGridView(channel = PreviewTestData.testProgramWithPrograms)
    }
}
