package com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelFavoriteRepository

interface ToggleFavoriteChannelUseCase {
    suspend operator fun invoke(playlistId: String, channel: TvChannel, type: FavoriteType)
}

class ToggleFavoriteChannelUseCaseImpl(
    private val favoriteChannelsRepository: ChannelFavoriteRepository,
) : ToggleFavoriteChannelUseCase {
    override suspend operator fun invoke(
        playlistId: String,
        channel: TvChannel,
        type: FavoriteType
    ) {
        when {
            channel.favoriteType == FavoriteType.NONE && type == FavoriteType.NONE -> Unit

            type == FavoriteType.NONE -> favoriteChannelsRepository.deleteChannelFromFavorite(
                playlistId = playlistId,
                channelUrl = channel.channelUrl,
            )

            channel.favoriteType == FavoriteType.NONE -> favoriteChannelsRepository.addChannelToFavorite(
                playlistId = playlistId,
                channelName = channel.channelName,
                channelUrl = channel.channelUrl,
                favoriteType = type,
            )

            channel.favoriteType == type ->
                favoriteChannelsRepository.deleteChannelFromFavorite(
                    playlistId = playlistId,
                    channelUrl = channel.channelUrl,
                )

            else -> favoriteChannelsRepository.updateFavoriteType(
                playlistId = playlistId,
                channelUrl = channel.channelUrl,
                favoriteType = type,
            )
        }
    }
}
