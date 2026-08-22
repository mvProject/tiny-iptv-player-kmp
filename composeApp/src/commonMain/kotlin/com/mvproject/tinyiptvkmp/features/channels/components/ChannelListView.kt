/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 20:26
 *
 */

package com.mvproject.tinyiptvkmp.features.channels.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptvkmp.core.domain.PreviewTestData
import com.mvproject.tinyiptvkmp.core.domain.enums.FavoriteType
import com.mvproject.tinyiptvkmp.core.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.core.theme.AppTheme
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.core.theme.dimensionWeight
import com.mvproject.tinyiptvkmp.core.ui.buttons.FavoriteButton
import com.mvproject.tinyiptvkmp.core.ui.indicators.ProgramProgressIndicator
import com.mvproject.tinyiptvkmp.core.ui.texts.ChannelTitle
import com.mvproject.tinyiptvkmp.core.ui.texts.EmptyProgramTitle
import com.mvproject.tinyiptvkmp.core.ui.texts.ProgramTitle
import org.jetbrains.compose.resources.stringResource
import tinyiptvkmp.composeapp.generated.resources.Res
import tinyiptvkmp.composeapp.generated.resources.msg_no_epg_found

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChannelListView(
    modifier: Modifier = Modifier,
    channel: TvChannel,
    onChannelSelect: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onShowEpgClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .combinedClickable(
                onClick = onChannelSelect,
                onLongClick = onShowEpgClick,
            )
            .clip(MaterialTheme.shapes.extraSmall),
    ) {
        Row(
            modifier = Modifier.padding(MaterialTheme.dimensionSize.size8),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensionSize.size8)
        ) {
            ChannelLogo(
                channelLogo = channel.channelLogo,
                channelName = channel.channelName,
            )
            Column(
                modifier = Modifier.weight(MaterialTheme.dimensionWeight.weight1),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensionSize.size4)
            ) {
                ChannelTitle(
                    title = channel.channelName,
                    isFavorite = channel.favoriteType != FavoriteType.NONE
                )
                if (channel.programs.isEmpty()) {
                    EmptyProgramTitle(title = stringResource(Res.string.msg_no_epg_found))
                } else {
                    ProgramTitle(title = channel.programs.first().title)
                }
            }
            FavoriteButton(
                isFavorite = channel.favoriteType != FavoriteType.NONE,
                onClick = onFavoriteClick
            )
        }

        if (channel.programs.isNotEmpty()) {
            ProgramProgressIndicator(progress = channel.programs.first().programProgress)
        }
    }
}

@Preview
@Composable
private fun ChannelListViewPreview() {
    AppTheme {
        ChannelListView(channel = PreviewTestData.testProgram.copy(programs = PreviewTestData.testEpgPrograms))
    }
}
