package com.mvproject.tinyiptvkmp.persistence.channels.room.mapper

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.persistence.channels.room.database.FavoriteChannelEntity

internal fun FavoriteChannelEntity.toFavoriteChannel() =
    FavoriteChannel(
        channelUrl = channelUrl,
        favoriteType = runCatching { FavoriteType.valueOf(favoriteType) }
            .getOrDefault(FavoriteType.NONE),
    )

internal fun favoriteChannelEntity(
    channelName: String,
    channelUrl: String,
    channelOrder: Long,
    favoriteType: FavoriteType,
    playlistId: String,
) = FavoriteChannelEntity(
    channelName = channelName,
    channelUrl = channelUrl,
    channelOrder = channelOrder,
    favoriteType = favoriteType.name,
    parentListId = playlistId,
)
