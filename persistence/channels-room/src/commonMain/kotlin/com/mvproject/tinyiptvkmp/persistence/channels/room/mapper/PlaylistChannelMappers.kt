package com.mvproject.tinyiptvkmp.persistence.channels.room.mapper

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannel
import com.mvproject.tinyiptvkmp.persistence.channels.room.database.PlaylistChannelEntity

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

