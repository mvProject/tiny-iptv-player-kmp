package com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelFavoriteRepository
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.PlaylistChannelRepository
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistRepository

interface DeletePlaylistUseCase {
    suspend operator fun invoke(playlist: Playlist)
}

internal class DeletePlaylistUseCaseImpl(
    private val playlistRepository: PlaylistRepository,
    private val playlistChannelRepository: PlaylistChannelRepository,
    private val channelFavoriteRepository: ChannelFavoriteRepository,
) : DeletePlaylistUseCase {
    override suspend operator fun invoke(playlist: Playlist) {
        if (playlist.isSelected) {
            val updateSelected =
                playlistRepository
                    .getAllPlaylists()
                    .firstOrNull { !it.isSelected }

            updateSelected?.let { selected ->
                playlistRepository.savePlaylist(selected.copy(isSelected = true))
            }
        }
        playlistRepository.deletePlaylist(playlist = playlist)

        channelFavoriteRepository.deletePlaylistFavoriteChannels(
            playlistId = playlist.id,
        )

        playlistChannelRepository.deletePlaylistChannels(
            listId = playlist.id,
        )
    }
}
