package com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistRepository

interface SelectPlaylistUseCase {
    suspend operator fun invoke(playlistId: String)
}

internal class SelectPlaylistUseCaseImpl(
    private val playlistRepository: PlaylistRepository,
) : SelectPlaylistUseCase {
    override suspend operator fun invoke(playlistId: String) {
        playlistRepository.selectPlaylist(id = playlistId)
    }
}
