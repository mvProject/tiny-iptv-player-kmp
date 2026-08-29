package com.mvproject.tinyiptvkmp.core.components.channels

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mvproject.tinyiptvkmp.core.foundation.common.INT_VALUE_1
import com.mvproject.tinyiptvkmp.core.foundation.model.ChannelsViewType
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize

@Composable
fun ChannelView(
    viewType: ChannelsViewType,
    items: List<ChannelItemUiModel>,
    modifier: Modifier = Modifier,
    onChannelSelect: (ChannelItemUiModel) -> Unit = {},
    onFavoriteClick: (ChannelItemUiModel) -> Unit = {},
    onShowProgramsClick: (ChannelItemUiModel) -> Unit = {},
    gridMinCellWidth: Dp = 180.dp,
    contentPadding: PaddingValues? = null,
) {
    val columns = remember(viewType, gridMinCellWidth) {
        when (viewType) {
            ChannelsViewType.LIST -> GridCells.Fixed(INT_VALUE_1)
            else -> GridCells.Adaptive(gridMinCellWidth)
        }
    }

    LazyVerticalGrid(
        modifier = modifier.clipToBounds(),
        columns = columns,
        state = rememberLazyGridState(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensionSize.size4),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensionSize.size4),
        contentPadding = contentPadding
            ?: PaddingValues(vertical = MaterialTheme.dimensionSize.size4),
    ) {
        items(
            items = items,
            key = { channel -> channel.id },
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
    }
}
