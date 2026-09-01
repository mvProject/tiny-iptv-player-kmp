package com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistRepository

interface DeletePlaylistUseCase {
    suspend operator fun invoke(playlist: Playlist)
}

internal class DeletePlaylistUseCaseImpl(
    private val playlistRepository: PlaylistRepository,
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
    }
}
