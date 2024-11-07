/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 20:26
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels.components

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
import com.mvproject.tinyiptvkmp.core.common.PreviewTestData
import com.mvproject.tinyiptvkmp.core.domain.model.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.core.theme.VideoAppTheme
import com.mvproject.tinyiptvkmp.core.theme.dimens
import com.mvproject.tinyiptvkmp.core.ui.buttons.FavoriteButton
import com.mvproject.tinyiptvkmp.core.ui.indicators.ProgramProgressIndicator
import com.mvproject.tinyiptvkmp.core.ui.texts.ChannelTitle
import com.mvproject.tinyiptvkmp.core.ui.texts.EmptyProgramTitle
import com.mvproject.tinyiptvkmp.core.ui.texts.ProgramTitle
import com.mvproject.tinyiptvkmp.data.enums.FavoriteType
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChannelListView(
    modifier: Modifier = Modifier,
    channel: TvPlaylistChannel,
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
            modifier = Modifier.padding(MaterialTheme.dimens.size8),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size8)
        ) {
            ChannelLogo(
                channelLogo = channel.channelLogo,
                channelName = channel.channelName,
            )
            Column(
                modifier = Modifier.weight(MaterialTheme.dimens.weight1),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size4)
            ) {
                ChannelTitle(
                    title = channel.channelName,
                    isFavorite = channel.favoriteType != FavoriteType.NONE
                )
                if (channel.programs.isEmpty()) {
                    EmptyProgramTitle()
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
    VideoAppTheme {
        ChannelListView(channel = PreviewTestData.testProgram.copy(programs = PreviewTestData.testEpgPrograms))
    }
}
