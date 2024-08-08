/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 19:45
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.data.enums.ChannelsViewType
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel

@Composable
fun ChannelView(
    modifier: Modifier = Modifier,
    viewType: ChannelsViewType,
    item: TvPlaylistChannel,
    onChannelSelect: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onShowEpgClick: () -> Unit = {},
) {
    when (viewType) {
        ChannelsViewType.LIST -> {
            ChannelListView(
                modifier = modifier,
                channel = item,
                onChannelSelect = onChannelSelect,
                onFavoriteClick = onFavoriteClick,
                onShowEpgClick = onShowEpgClick,
            )
        }

        ChannelsViewType.GRID -> {
            ChannelGridView(
                modifier = modifier,
                channel = item,
                onChannelSelect = onChannelSelect,
                onFavoriteClick = onFavoriteClick,
                onShowEpgClick = onShowEpgClick,
            )
        }

        ChannelsViewType.CARD -> {
            ChannelCardView(
                modifier = modifier,
                channel = item,
                onChannelSelect = onChannelSelect,
                onFavoriteClick = onFavoriteClick,
                onShowEpgClick = onShowEpgClick,
            )
        }
    }
}
