package com.mvproject.tinyiptvkmp.features.channels.api.domain.utils

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.TvChannel

fun TvChannel.toggleFavorite(
    type: String,
): TvChannel {
    val favType =
        if (this.favoriteType == type) {
            FAVORITE_TYPE_NONE
        } else {
            type
        }
    return this.copy(favoriteType = favType)
}

fun List<TvChannel>.replaceUpdated(
    channel: TvChannel
): List<TvChannel> {
    val index = this.indexOfFirst { it.channelName == channel.channelName }
    return this.toMutableList().apply { set(index, channel) }
}

private const val FAVORITE_TYPE_NONE = "NONE"
