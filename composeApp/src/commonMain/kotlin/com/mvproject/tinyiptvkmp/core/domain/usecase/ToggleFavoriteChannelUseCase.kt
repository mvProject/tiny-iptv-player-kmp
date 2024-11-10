package com.mvproject.tinyiptvkmp.core.domain.usecase

import com.mvproject.tinyiptvkmp.core.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptvkmp.core.domain.enums.FavoriteType
import com.mvproject.tinyiptvkmp.core.domain.model.TvChannel

class ToggleFavoriteChannelUseCase(
    private val favoriteChannelsRepository: FavoriteChannelsRepository,
) {
    suspend operator fun invoke(channel: TvChannel) {
        val isFavorite = channel.favoriteType != FavoriteType.NONE

        if (isFavorite) {
            favoriteChannelsRepository.deleteChannelFromFavorite(channelUrl = channel.channelUrl)
        } else {
            favoriteChannelsRepository.addChannelToFavorite(
                channelName = channel.channelName,
                channelUrl = channel.channelUrl,
                favoriteType = channel.favoriteType,
            )
        }
    }
}