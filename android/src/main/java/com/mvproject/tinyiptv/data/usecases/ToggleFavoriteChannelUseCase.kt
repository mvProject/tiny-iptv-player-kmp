/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 03.09.23, 21:48
 *
 */

package com.mvproject.tinyiptv.data.usecases

import com.mvproject.tinyiptv.data.models.channels.TvPlaylistChannel
import com.mvproject.tinyiptv.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptv.data.repository.PreferenceRepository

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