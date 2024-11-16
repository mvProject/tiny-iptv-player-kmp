/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 19:45
 *
 */

package com.mvproject.tinyiptvkmp.features.channels.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp
import com.mvproject.tinyiptvkmp.core.common.AppConstants.INT_VALUE_1
import com.mvproject.tinyiptvkmp.core.domain.enums.ChannelsViewType
import com.mvproject.tinyiptvkmp.core.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize

@Composable
fun ChannelView(
    modifier: Modifier = Modifier,
    viewType: ChannelsViewType,
    items: List<TvChannel>,
    onChannelSelect: (TvChannel) -> Unit = {},
    onFavoriteClick: (TvChannel) -> Unit = {},
    onShowProgramsClick: (TvChannel) -> Unit = {},
) {

    // todo adaptive size depend on windowSizeClass

    val columns = remember(viewType) {
        when (viewType) {
            ChannelsViewType.LIST -> GridCells.Fixed(INT_VALUE_1)
            else -> GridCells.Adaptive(180.dp)
        }
    }

    LazyVerticalGrid(
        modifier = modifier.clipToBounds(),
        columns = columns,
        state = rememberLazyGridState(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensionSize.size4),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensionSize.size4),
        contentPadding = PaddingValues(vertical = MaterialTheme.dimensionSize.size4),
        content = {
            items(
                items = items,
                key = { chn -> "${chn.channelName}${chn.channelUrl}" },
            ) { item ->
                when (viewType) {
                    ChannelsViewType.LIST -> {
                        ChannelListView(
                            modifier = Modifier.fillMaxSize().animateItem(),
                            channel = item,
                            onChannelSelect = { onChannelSelect(item) },
                            onFavoriteClick = { onFavoriteClick(item) },
                            onShowEpgClick = { onShowProgramsClick(item) },
                        )
                    }

                    ChannelsViewType.GRID -> {
                        ChannelGridView(
                            modifier = Modifier.fillMaxSize().animateItem(),
                            channel = item,
                            onChannelSelect = { onChannelSelect(item) },
                            onFavoriteClick = { onFavoriteClick(item) },
                            onShowEpgClick = { onShowProgramsClick(item) },
                        )
                    }

                    ChannelsViewType.CARD -> {
                        ChannelCardView(
                            modifier = Modifier.fillMaxSize().animateItem(),
                            channel = item,
                            onChannelSelect = { onChannelSelect(item) },
                            onFavoriteClick = { onFavoriteClick(item) },
                            onShowEpgClick = { onShowProgramsClick(item) },
                        )
                    }
                }
            }
        },
    )
}
