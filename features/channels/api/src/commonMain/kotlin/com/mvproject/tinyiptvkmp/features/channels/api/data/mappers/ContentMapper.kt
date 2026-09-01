package com.mvproject.tinyiptvkmp.features.channels.api.data.mappers

import com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.content.PlaylistChannelParseModel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannel

internal fun PlaylistChannelParseModel.toPlaylistChannel(id: String) =
    PlaylistChannel(
        channelName = channel,
        channelLogo = logoURL,
        channelUrl = streamURL,
        channelGroup = groupTitle,
        parentListId = id,
    )