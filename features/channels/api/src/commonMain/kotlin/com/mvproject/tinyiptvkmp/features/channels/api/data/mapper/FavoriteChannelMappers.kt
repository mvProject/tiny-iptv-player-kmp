package com.mvproject.tinyiptvkmp.features.channels.api.data.mapper

import com.mvproject.tinyiptvkmp.features.channels.api.data.local.database.FavoriteChannelEntity
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteChannel

internal fun FavoriteChannelEntity.toFavoriteChannel() =
    FavoriteChannel(
        channelUrl = channelUrl,
        favoriteType = favoriteType,
    )

internal fun favoriteChannelEntity(
    channelName: String,
    channelUrl: String,
    channelOrder: Long,
    favoriteType: String,
    playlistId: String,
) = FavoriteChannelEntity(
    channelName = channelName,
    channelUrl = channelUrl,
    channelOrder = channelOrder,
    favoriteType = favoriteType,
    parentListId = playlistId,
)
