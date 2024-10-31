/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.07.24, 18:06
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.enums.FavoriteType
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.data.repository.FavoriteChannelsRepository

class ToggleFavoriteChannelUseCase(
    private val favoriteChannelsRepository: FavoriteChannelsRepository,
) {
    suspend operator fun invoke(
        channel: TvPlaylistChannel,
        favoriteType: FavoriteType = FavoriteType.COMMON,
    ) {
        val isFavorite = channel.favoriteType != FavoriteType.NONE

        if (isFavorite) {
            favoriteChannelsRepository.deleteChannelFromFavorite(channelUrl = channel.channelUrl)
        } else {
            favoriteChannelsRepository.addChannelToFavorite(
                channel = channel,
                favoriteType = favoriteType,
            )
        }
    }
}
