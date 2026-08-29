/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 14:32
 *
 */

package com.mvproject.tinyiptvkmp.features.player.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import com.mvproject.tinyiptvkmp.core.theme.dimensionFraction
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.TvChannelWithPrograms

@Composable
fun PlayerChannels(
    group: String,
    channels: List<TvChannelWithPrograms> = emptyList(),
    current: Int = 0,
    onChannelSelect: (TvChannelWithPrograms) -> Unit = {},
) {
    val listState = rememberLazyListState()
    LaunchedEffect(key1 = current) {
        listState.animateScrollToItem(current)
    }

    Column(
        modifier =
            Modifier
                .fillMaxHeight(MaterialTheme.dimensionFraction.fraction90)
                .fillMaxWidth(MaterialTheme.dimensionFraction.fraction80)
                .background(
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
            modifier = Modifier.fillMaxSize(),
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
// todo replace preview
/*
@Composable
@Preview(showBackground = true)
fun DarkPreviewOOverlayChannels() {
    VideoAppTheme(darkTheme = true) {
        OverlayChannels(
            group = "TestGroup",
            channels = listOf(
                TvPlaylistChannel(
                    channelName = "test1",
                    channelEpg = listOf(
                        EpgProgram(
                            title = "Epg1",
                            channelId = "id1",
                            start = System.currentTimeMillis() - 15.minutes.inWholeMilliseconds,
                            stop = System.currentTimeMillis() + 15.minutes.inWholeMilliseconds,
                            description = "Epg Description"
                        )
                    )
                ),
                TvPlaylistChannel(
                    channelName = "test2",
                    channelEpg = listOf(
                        EpgProgram(
                            title = "Epg2",
                            channelId = "id2",
                            start = System.currentTimeMillis() + 15.minutes.inWholeMilliseconds,
                            stop = System.currentTimeMillis() + 30.minutes.inWholeMilliseconds,
                            description = "Epg Description"
                        )
                    )
                )
            )
        )
    }
}*/
