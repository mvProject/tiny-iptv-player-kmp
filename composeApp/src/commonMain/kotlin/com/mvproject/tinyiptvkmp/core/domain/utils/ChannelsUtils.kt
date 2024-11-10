package com.mvproject.tinyiptvkmp.core.domain.utils

import com.mvproject.tinyiptvkmp.core.domain.enums.FavoriteType
import com.mvproject.tinyiptvkmp.core.domain.model.EpgProgram
import com.mvproject.tinyiptvkmp.core.domain.model.TvChannel

object ChannelsUtils {
    fun List<TvChannel>.mapProgramIds() = this
        .map { it.programId }
        .toSet()
        .filter { it.isNotBlank() }

    fun List<TvChannel>.mapPrograms(channelEpgMap: ChannelEpgMap) = this.map { channel ->
        val programs = channelEpgMap[channel.programId] ?: emptyList()
        channel.copy(programs = programs)
    }

    fun TvChannel.toggleFavorite(
        type: FavoriteType
    ): TvChannel {
        val favType =
            if (this.favoriteType == type) {
                FavoriteType.NONE
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
}

typealias ChannelEpgMap = Map<String, List<EpgProgram>>