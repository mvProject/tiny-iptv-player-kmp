package com.mvproject.tinyiptvkmp.core.domain.usecase

import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.core.domain.model.Playlist

class SelectPlaylistUseCase(
    private val playlistsRepository: PlaylistsRepository,
) {
    suspend operator fun invoke(playlist: Playlist) {
        val playlists = playlistsRepository.getAllPlaylists()
        val changedPlaylists =
            playlists.map { it.copy(isSelected = it.id == playlist.id) }
        playlistsRepository.savePlaylists(playlists = changedPlaylists)
    }
}