package com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelFavoriteRepository
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.PlaylistChannelRepository

interface DeletePlaylistContentUseCase {
    suspend operator fun invoke(playlistId: String)
}

internal class DeletePlaylistContentUseCaseImpl(
    private val playlistChannelRepository: PlaylistChannelRepository,
    private val channelFavoriteRepository: ChannelFavoriteRepository,
) : DeletePlaylistContentUseCase {
    override suspend operator fun invoke(playlistId: String) {
        channelFavoriteRepository.deletePlaylistFavoriteChannels(
            playlistId = playlistId,
        )

        playlistChannelRepository.deletePlaylistChannels(
            listId = playlistId,
        )
    }
}
