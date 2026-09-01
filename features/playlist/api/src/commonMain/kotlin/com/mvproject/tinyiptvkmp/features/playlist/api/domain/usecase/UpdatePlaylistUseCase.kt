package com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.PlaylistType
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistRepository

interface UpdatePlaylistUseCase {
    suspend operator fun invoke(playlist: Playlist): UpdatePlaylistResult
}

data class UpdatePlaylistResult(
    val playlist: Playlist,
    val contentRefreshRequired: Boolean,
)

internal class UpdatePlaylistUseCaseImpl(
    private val playlistRepository: PlaylistRepository,
) : UpdatePlaylistUseCase {
    override suspend operator fun invoke(playlist: Playlist): UpdatePlaylistResult {
        val existing = playlistRepository.getPlaylistById(id = playlist.id)
        val updated = existing.updatedWith(playlist)
        val shouldReloadContent =
            existing.playlistType == PlaylistType.REMOTE &&
                    existing.playlistSource != updated.playlistSource

        playlistRepository.savePlaylist(playlist = updated)

        return UpdatePlaylistResult(
            playlist = updated,
            contentRefreshRequired = shouldReloadContent,
        )
    }
}

private fun Playlist.updatedWith(candidate: Playlist): Playlist =
    when (playlistType) {
        PlaylistType.LOCAL ->
            copy(
                playlistName = candidate.playlistName,
            )

        PlaylistType.REMOTE ->
            copy(
                playlistName = candidate.playlistName,
                playlistSource = candidate.playlistSource,
                updatePeriod = candidate.updatePeriod,
            )
    }
