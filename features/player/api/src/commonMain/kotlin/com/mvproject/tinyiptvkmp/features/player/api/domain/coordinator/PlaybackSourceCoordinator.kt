package com.mvproject.tinyiptvkmp.features.player.api.domain.coordinator

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.features.player.api.domain.model.PlaybackSource
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface PlaybackSourceCoordinator {
    val source: StateFlow<PlaybackSource?>

    val updates: SharedFlow<PlaybackSource>

    fun setSource(source: PlaybackSource)

    fun updateSelectedChannel(channel: TvChannel, selectedIndex: Int)

    fun appendLoadedChannels(
        channels: List<TvChannel>,
        nextOffset: Int,
        hasMore: Boolean,
    )

    fun replaceLoadedChannels(
        channels: List<TvChannel>,
        selectedIndex: Int,
        nextOffset: Int,
        hasMore: Boolean,
    )
}
