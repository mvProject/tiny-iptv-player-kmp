package com.mvproject.tinyiptvkmp.features.player.api.domain.model

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.ChannelGroupSelection

data class PlaybackSource(
    val playlistId: String,
    val group: String,
    val groupType: String,
    val selection: ChannelGroupSelection,
    val selectedChannel: TvChannel,
    val selectedIndex: Int,
    val loadedChannels: List<TvChannel>,
    val nextChannelsOffset: Int,
    val hasMoreChannels: Boolean,
)
