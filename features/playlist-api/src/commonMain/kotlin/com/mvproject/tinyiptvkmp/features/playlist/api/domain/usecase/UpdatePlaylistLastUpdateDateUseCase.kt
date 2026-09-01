package com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistRepository
import kotlin.time.Clock

interface UpdatePlaylistLastUpdateDateUseCase {
    suspend operator fun invoke(
        playlistId: String,
        lastUpdateDate: Long = Clock.System.now().toEpochMilliseconds(),
    )
}

internal class UpdatePlaylistLastUpdateDateUseCaseImpl(
    private val playlistRepository: PlaylistRepository,
) : UpdatePlaylistLastUpdateDateUseCase {
    override suspend operator fun invoke(
        playlistId: String,
        lastUpdateDate: Long,
    ) {
        val playlist = playlistRepository.getPlaylistById(id = playlistId)
        playlistRepository.savePlaylist(
            playlist = playlist.copy(lastUpdateDate = lastUpdateDate),
        )
    }
}
