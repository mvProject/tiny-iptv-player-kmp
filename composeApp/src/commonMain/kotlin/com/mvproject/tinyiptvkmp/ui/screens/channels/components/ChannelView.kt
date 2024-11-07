/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 19:45
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mvproject.tinyiptvkmp.core.domain.model.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.core.ui.theme.dimens
import com.mvproject.tinyiptvkmp.data.enums.ChannelsViewType
import com.mvproject.tinyiptvkmp.utils.AppConstants.INT_VALUE_1

@Composable
fun ChannelView(
    modifier: Modifier = Modifier,
    viewType: ChannelsViewType,
    items: List<TvPlaylistChannel>,
    onChannelSelect: (TvPlaylistChannel) -> Unit = {},
    onFavoriteClick: (TvPlaylistChannel) -> Unit = {},
    onShowEpgClick: (TvPlaylistChannel) -> Unit = {},
) {
    val columns = remember(viewType) {
        when (viewType) {
            ChannelsViewType.LIST -> GridCells.Fixed(INT_VALUE_1)
            else -> GridCells.Adaptive(180.dp)
        }
    }

    LazyVerticalGrid(
        modifier = Modifier.fillMaxHeight(),
        columns = columns,
        state = rememberLazyGridState(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size4),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size4),
        contentPadding = PaddingValues(vertical = MaterialTheme.dimens.size4),
        content = {
            items(
                items = items,
                key = { chn -> chn.hashCode() },
            ) { item ->
                when (viewType) {
                    ChannelsViewType.LIST -> {
                        ChannelListView(
                            modifier = modifier,
                            channel = item,
                            onChannelSelect = { onChannelSelect(item) },
                            onFavoriteClick = { onFavoriteClick(item) },
                            onShowEpgClick = { onShowEpgClick(item) },
                        )
                    }

                    ChannelsViewType.GRID -> {
                        ChannelGridView(
                            modifier = modifier,
                            channel = item,
                            onChannelSelect = { onChannelSelect(item) },
                            onFavoriteClick = { onFavoriteClick(item) },
                            onShowEpgClick = { onShowEpgClick(item) },
                        )
                    }

                    ChannelsViewType.CARD -> {
                        ChannelCardView(
                            modifier = modifier,
                            channel = item,
                            onChannelSelect = { onChannelSelect(item) },
                            onFavoriteClick = { onFavoriteClick(item) },
                            onShowEpgClick = { onShowEpgClick(item) },
                        )
                    }
                }
            }
        },
    )
}
