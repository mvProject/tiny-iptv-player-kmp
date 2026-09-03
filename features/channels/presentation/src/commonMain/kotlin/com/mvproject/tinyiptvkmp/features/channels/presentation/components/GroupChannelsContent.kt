package com.mvproject.tinyiptvkmp.features.channels.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mvproject.tinyiptvkmp.core.components.channels.ChannelCardView
import com.mvproject.tinyiptvkmp.core.components.channels.ChannelGridView
import com.mvproject.tinyiptvkmp.core.components.channels.ChannelItemUiModel
import com.mvproject.tinyiptvkmp.core.components.channels.ChannelListView
import com.mvproject.tinyiptvkmp.core.foundation.common.INT_VALUE_1
import com.mvproject.tinyiptvkmp.core.foundation.model.ChannelsViewType
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull

@Composable
internal fun GroupChannelsContent(
    viewType: ChannelsViewType,
    items: List<ChannelItemUiModel>,
    modifier: Modifier = Modifier,
    onChannelSelect: (ChannelItemUiModel) -> Unit = {},
    onFavoriteClick: (ChannelItemUiModel) -> Unit = {},
    onShowProgramsClick: (ChannelItemUiModel) -> Unit = {},
    onEndReached: () -> Unit = {},
    gridMinCellWidth: Dp = 180.dp,
    contentPadding: PaddingValues? = null,
) {
    val gridState = rememberLazyGridState()
    val latestOnEndReached by rememberUpdatedState(onEndReached)
    val columns = remember(viewType, gridMinCellWidth) {
        when (viewType) {
            ChannelsViewType.LIST -> GridCells.Fixed(INT_VALUE_1)
            else -> GridCells.Adaptive(gridMinCellWidth)
        }
    }

    LaunchedEffect(gridState) {
        snapshotFlow {
            val layoutInfo = gridState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            if (
                totalItemsCount > 0 &&
                lastVisibleIndex >= totalItemsCount - END_REACHED_THRESHOLD
            ) {
                totalItemsCount
            } else {
                null
            }
        }
            .distinctUntilChanged()
            .filterNotNull()
            .collect { latestOnEndReached() }
    }

    LazyVerticalGrid(
        modifier = modifier.clipToBounds(),
        columns = columns,
        state = gridState,
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

private const val END_REACHED_THRESHOLD = 10
