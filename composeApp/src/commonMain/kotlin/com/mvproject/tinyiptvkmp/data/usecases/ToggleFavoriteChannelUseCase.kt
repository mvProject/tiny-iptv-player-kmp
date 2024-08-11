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
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository

class ToggleFavoriteChannelUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val favoriteChannelsRepository: FavoriteChannelsRepository,
) {
    suspend operator fun invoke(
        channel: TvPlaylistChannel,
        favoriteType: FavoriteType = FavoriteType.COMMON,
    ) {
        val currentPlaylistId = preferenceRepository.loadCurrentPlaylistId()

        val isFavorite = channel.favoriteType != FavoriteType.NONE

        if (isFavorite) {
            favoriteChannelsRepository.deleteChannelFromFavorite(
                channelUrl = channel.channelUrl,
                listId = currentPlaylistId,
            )
        } else {
            favoriteChannelsRepository.addChannelToFavorite(
                channel = channel,
                listId = currentPlaylistId,
                favoriteType = favoriteType,
            )
        }
    }
}
