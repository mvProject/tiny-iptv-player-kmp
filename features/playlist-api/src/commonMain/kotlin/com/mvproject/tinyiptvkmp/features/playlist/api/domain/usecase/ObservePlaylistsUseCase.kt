package com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

interface ObservePlaylistsUseCase {
    operator fun invoke(): Flow<List<Playlist>>
}

internal class ObservePlaylistsUseCaseImpl(
    private val playlistRepository: PlaylistRepository,
) : ObservePlaylistsUseCase {
    override operator fun invoke(): Flow<List<Playlist>> =
        playlistRepository.observePlaylists()
}
