package com.mvproject.tinyiptvkmp.features.epg.api.domain.utils

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgProgram
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.TvChannelWithPrograms
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.withPrograms

fun List<TvChannel>.withPrograms() =
    map { channel -> channel.withPrograms() }

fun List<TvChannelWithPrograms>.mapProgramIds() = this
    .map { it.programId }
    .toSet()
    .filter { it.isNotBlank() }

fun List<TvChannelWithPrograms>.mapPrograms(channelEpgMap: ChannelEpgMap) = this.map { item ->
    item.copy(programs = channelEpgMap[item.programId] ?: emptyList())
}

fun TvChannelWithPrograms.toggleFavorite(
    type: FavoriteType,
): TvChannelWithPrograms {
    val favType =
        if (favoriteType == type) {
            FavoriteType.NONE
        } else {
            type
        }
    return copy(channel = channel.copy(favoriteType = favType))
}

fun List<TvChannelWithPrograms>.replaceUpdated(
    channel: TvChannelWithPrograms,
): List<TvChannelWithPrograms> {
    val index = indexOfFirst { it.channelName == channel.channelName }
    if (index < 0) return this
    return toMutableList().apply { set(index, channel) }
}

typealias ChannelEpgMap = Map<String, List<EpgProgram>>
