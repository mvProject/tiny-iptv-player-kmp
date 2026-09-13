package com.mvproject.tinyiptvkmp.features.player.api.data.coordinator

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.features.player.api.domain.coordinator.PlaybackSourceCoordinator
import com.mvproject.tinyiptvkmp.features.player.api.domain.model.PlaybackSource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class PlaybackSourceCoordinatorImpl : PlaybackSourceCoordinator {
    private val _source = MutableStateFlow<PlaybackSource?>(null)
    private val _updates = MutableSharedFlow<PlaybackSource>(
        replay = 1,
        extraBufferCapacity = 1,
    )

    override val source: StateFlow<PlaybackSource?> = _source.asStateFlow()

    override val updates: SharedFlow<PlaybackSource> = _updates.asSharedFlow()

    override fun setSource(source: PlaybackSource) {
        if (_source.value == source) {
            return
        }
        _source.value = source
        _updates.tryEmit(source)
    }

    override fun updateSelectedChannel(channel: TvChannel, selectedIndex: Int) {
        val previous = _source.value
        _source.update { current ->
            if (current == null ||
                current.selectedChannel == channel &&
                current.selectedIndex == selectedIndex
            ) {
                return@update current
            }
            current.copy(
                selectedChannel = channel,
                selectedIndex = selectedIndex,
                loadedChannels = current.loadedChannels.replaceUpdated(channel),
            )
        }
        if (_source.value != previous) emitCurrentSource()
    }

    override fun appendLoadedChannels(
        channels: List<TvChannel>,
        nextOffset: Int,
        hasMore: Boolean,
    ) {
        val previous = _source.value
        _source.update { current ->
            current ?: return@update null

            val loadedChannels = current.loadedChannels.appendDistinct(channels)
            if (
                loadedChannels == current.loadedChannels &&
                current.nextChannelsOffset == nextOffset &&
                current.hasMoreChannels == hasMore
            ) {
                return@update current
            }

            current.copy(
                loadedChannels = loadedChannels,
                nextChannelsOffset = nextOffset,
                hasMoreChannels = hasMore,
            )
        }
        if (_source.value != previous) emitCurrentSource()
    }

    override fun replaceLoadedChannels(
        channels: List<TvChannel>,
        selectedIndex: Int,
        nextOffset: Int,
        hasMore: Boolean,
    ) {
        val previous = _source.value
        _source.update { current ->
            if (current == null ||
                current.loadedChannels == channels &&
                current.selectedIndex == selectedIndex &&
                current.nextChannelsOffset == nextOffset &&
                current.hasMoreChannels == hasMore
            ) {
                return@update current
            }
            current.copy(
                selectedIndex = selectedIndex,
                loadedChannels = channels,
                nextChannelsOffset = nextOffset,
                hasMoreChannels = hasMore,
            )
        }
        if (_source.value != previous) emitCurrentSource()
    }

    private fun emitCurrentSource() {
        _source.value?.let { source -> _updates.tryEmit(source) }
    }
}

private fun List<TvChannel>.appendDistinct(channels: List<TvChannel>): List<TvChannel> {
    val existingUrls = mapTo(mutableSetOf()) { channel -> channel.channelUrl }
    val newChannels = channels.filter { channel -> existingUrls.add(channel.channelUrl) }
    return this + newChannels
}

private fun List<TvChannel>.replaceUpdated(channel: TvChannel): List<TvChannel> {
    val index = indexOfFirst { item -> item.channelUrl == channel.channelUrl }
    if (index < 0) return this
    return toMutableList().apply { set(index, channel) }
}
