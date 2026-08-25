package com.mvproject.tinyiptvkmp.core.domain.mappers

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.FavoriteType

object Mapper {
    fun PlaylistChannel.toTvChannel(
        favoriteType: FavoriteType
    ) = with(this) {
        TvChannel(
            channelName = channelName,
            channelLogo = channelLogo,
            channelUrl = channelUrl,
            programId = programId,
            favoriteType = favoriteType.name
        )
    }

}
