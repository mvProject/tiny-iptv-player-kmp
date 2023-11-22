/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository

class ToggleFavoriteChannelUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val favoriteChannelsRepository: FavoriteChannelsRepository,
) {
    suspend operator fun invoke(
        channel: TvPlaylistChannel
    ) {
        val currentPlaylistId = preferenceRepository.loadCurrentPlaylistId()

        val isFavorite = channel.isInFavorites

        if (isFavorite) {
            favoriteChannelsRepository.deleteChannelFromFavorite(
                channelUrl = channel.channelUrl,
                listId = currentPlaylistId
            )

        } else {
            favoriteChannelsRepository.addChannelToFavorite(
                channel = channel,
                listId = currentPlaylistId
            )
        }
    }
}