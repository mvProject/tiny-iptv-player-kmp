package com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistRepository

interface SelectPlaylistUseCase {
    suspend operator fun invoke(playlist: Playlist)
}

internal class SelectPlaylistUseCaseImpl(
    private val playlistRepository: PlaylistRepository,
) : SelectPlaylistUseCase {
    override suspend operator fun invoke(playlist: Playlist) {
        val playlists = playlistRepository.getAllPlaylists()
        val changedPlaylists =
            playlists.map { it.copy(isSelected = it.id == playlist.id) }
        playlistRepository.savePlaylists(playlists = changedPlaylists)
    }
}
