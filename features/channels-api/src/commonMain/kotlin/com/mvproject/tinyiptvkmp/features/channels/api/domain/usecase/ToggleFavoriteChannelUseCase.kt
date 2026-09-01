package com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelFavoriteRepository

class ToggleFavoriteChannelUseCase(
    private val favoriteChannelsRepository: ChannelFavoriteRepository,
) {
    suspend operator fun invoke(playlistId: String, channel: TvChannel, type: String) {
        val isFavorite = channel.favoriteType != FAVORITE_TYPE_NONE

        if (isFavorite) {
            favoriteChannelsRepository.deleteChannelFromFavorite(
                playlistId = playlistId,
                channelUrl = channel.channelUrl,
            )
        } else {
            favoriteChannelsRepository.addChannelToFavorite(
                playlistId = playlistId,
                channelName = channel.channelName,
                channelUrl = channel.channelUrl,
                favoriteType = type,
            )
        }
    }

    private companion object {
        const val FAVORITE_TYPE_NONE = "NONE"
    }
}
