/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 13:43
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
    onOptionSelect: () -> Unit = {},
) {
    when (viewType) {
        ChannelsViewType.LIST -> {
            ChannelListView(
                modifier = modifier,
                channel = item,
                onChannelSelect = onChannelSelect,
                onOptionSelect = onOptionSelect,
            )
        }

        ChannelsViewType.GRID -> {
            ChannelGridView(
                modifier = modifier,
                channel = item,
                onChannelSelect = onChannelSelect,
                onOptionSelect = onOptionSelect,
            )
        }

        ChannelsViewType.CARD -> {
            ChannelCardView(
                modifier = modifier,
                channel = item,
                onChannelSelect = onChannelSelect,
                onOptionSelect = onOptionSelect,
            )
        }
    }
}
