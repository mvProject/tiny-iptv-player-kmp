package com.mvproject.tinyiptvkmp.core.domain.usecase

import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.core.domain.model.Playlist

class GetPlaylistUseCase(
    private val playlistsRepository: PlaylistsRepository,
) {
    suspend operator fun invoke(playlistId: String): Playlist {
        return if (playlistId.isNotBlank()) {
            playlistsRepository.getPlaylistById(id = playlistId)
        } else Playlist()
    }
}