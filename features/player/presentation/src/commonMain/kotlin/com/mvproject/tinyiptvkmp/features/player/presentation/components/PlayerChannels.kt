/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 14:32
 *
 */

package com.mvproject.tinyiptvkmp.features.player.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mvproject.tinyiptvkmp.core.components.channels.ChannelListView
import com.mvproject.tinyiptvkmp.core.components.modifiers.roundedHeader
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.TvChannelWithPrograms

@Composable
fun PlayerChannels(
    group: String,
    channels: List<TvChannelWithPrograms> = emptyList(),
    current: Int = 0,
    modifier: Modifier = Modifier,
    onChannelSelect: (TvChannelWithPrograms) -> Unit = {},
) {
    val listState = rememberLazyListState()
    LaunchedEffect(current, channels.size) {
        if (current !in channels.indices) return@LaunchedEffect

        val visibleRange = listState.layoutInfo.visibleItemsInfo
            .map { item -> item.index }
            .let { indexes -> indexes.minOrNull() to indexes.maxOrNull() }
        if (current in (visibleRange.first ?: current)..(visibleRange.second ?: current)) {
            return@LaunchedEffect
        }

        listState.scrollToItem(current)
    }

    Column(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.primary,
            shape = MaterialTheme.shapes.small,
        ),
    ) {
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .roundedHeader(),
            text = group,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(2.dp),
            content = {
                items(
                    items = channels,
                    key = { chn -> "${chn.channelName}${chn.channelUrl}" },
                ) { chn ->
                    ChannelListView(
                        channel = chn.toChannelItemUiModel(),
                        onChannelSelect = { onChannelSelect(chn) },
                    )
                }
            },
        )
    }
}
