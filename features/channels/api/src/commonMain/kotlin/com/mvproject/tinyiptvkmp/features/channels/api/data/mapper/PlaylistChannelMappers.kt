package com.mvproject.tinyiptvkmp.features.channels.api.data.mapper

import com.mvproject.tinyiptvkmp.features.channels.api.data.local.PlaylistChannelParseModel
import com.mvproject.tinyiptvkmp.features.channels.api.data.local.database.PlaylistChannelEntity
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannel

internal fun PlaylistChannelEntity.toPlaylistChannel() =
    PlaylistChannel(
        channelName = channelName,
        channelLogo = channelLogo,
        channelUrl = channelUrl,
        channelGroup = channelGroup,
        programId = programId,
        parentListId = parentListId,
    )

internal fun PlaylistChannel.toChannelEntity() =
    PlaylistChannelEntity(
        channelName = channelName,
        channelLogo = channelLogo,
        channelUrl = channelUrl,
        channelGroup = channelGroup,
        programId = programId,
        parentListId = parentListId,
    )

internal fun PlaylistChannelParseModel.toPlaylistChannel(id: String) =
    PlaylistChannel(
        channelName = channel,
        channelLogo = logoURL,
        channelUrl = streamURL,
        channelGroup = groupTitle,
        parentListId = id,
    )
