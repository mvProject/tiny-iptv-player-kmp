package com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistRepository

interface CreatePlaylistUseCase {
    suspend operator fun invoke(playlist: Playlist): Playlist
}

internal class CreatePlaylistUseCaseImpl(
    private val playlistRepository: PlaylistRepository,
) : CreatePlaylistUseCase {
    override suspend operator fun invoke(playlist: Playlist): Playlist {
        val savedPlaylist =
            playlist.copy(
                isSelected = playlistRepository.getAllPlaylists().isEmpty(),
            )

        playlistRepository.savePlaylist(playlist = savedPlaylist)
        return savedPlaylist
    }
}
