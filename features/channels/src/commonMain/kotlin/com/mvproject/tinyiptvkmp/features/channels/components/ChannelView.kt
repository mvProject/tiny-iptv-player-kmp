package com.mvproject.tinyiptvkmp.features.channels.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mvproject.tinyiptvkmp.core.foundation.model.ChannelsViewType
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.TvChannelWithPrograms
import com.mvproject.tinyiptvkmp.core.components.channels.ChannelView as DesignSystemChannelView

@Composable
fun ChannelView(
    modifier: Modifier = Modifier,
    viewType: ChannelsViewType,
    items: List<TvChannelWithPrograms>,
    onChannelSelect: (TvChannelWithPrograms) -> Unit = {},
    onFavoriteClick: (TvChannelWithPrograms) -> Unit = {},
    onShowProgramsClick: (TvChannelWithPrograms) -> Unit = {},
    gridMinCellWidth: Dp = 180.dp,
    contentPadding: PaddingValues? = null,
) {
    val channelItems = remember(items) {
        items.map { channel -> channel.toChannelItemUiModel() }
    }
    val channelById = remember(items) {
        items.associateBy { channel -> channel.toChannelItemUiModel().id }
    }

    DesignSystemChannelView(
        modifier = modifier,
        viewType = viewType,
        items = channelItems,
        gridMinCellWidth = gridMinCellWidth,
        contentPadding = contentPadding,
        onChannelSelect = { selected ->
            channelById[selected.id]?.let(onChannelSelect)
        },
        onFavoriteClick = { selected ->
            channelById[selected.id]?.let(onFavoriteClick)
        },
        onShowProgramsClick = { selected ->
            channelById[selected.id]?.let(onShowProgramsClick)
        },
    )
}
