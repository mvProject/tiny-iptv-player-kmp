package com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistRepository

interface GetPlaylistUseCase {
    suspend operator fun invoke(playlistId: String): Playlist
}

internal class GetPlaylistUseCaseImpl(
    private val playlistRepository: PlaylistRepository,
) : GetPlaylistUseCase {
    override suspend operator fun invoke(playlistId: String): Playlist {
        return if (playlistId.isNotBlank()) {
            playlistRepository.getPlaylistById(id = playlistId)
        } else Playlist()
    }
}
