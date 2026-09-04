package com.mvproject.tinyiptvkmp.persistence.channels.room.mapper

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.persistence.channels.room.database.PlaylistChannelEntity
import com.mvproject.tinyiptvkmp.persistence.channels.room.database.PlaylistChannelFavorite

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

internal fun PlaylistChannelFavorite.toTvChannel() =
    TvChannel(
        channelName = channelName,
        channelLogo = channelLogo,
        channelUrl = channelUrl,
        programId = programId,
        favoriteType = favoriteType.toFavoriteType(),
    )

private fun String?.toFavoriteType(): FavoriteType =
    this
        ?.let { value -> runCatching { FavoriteType.valueOf(value) }.getOrNull() }
        ?: FavoriteType.NONE
