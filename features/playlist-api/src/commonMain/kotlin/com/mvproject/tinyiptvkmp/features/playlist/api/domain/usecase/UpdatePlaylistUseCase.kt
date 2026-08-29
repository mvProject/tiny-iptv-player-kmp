package com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.PlaylistType
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistRepository
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistSyncStateRepository
import kotlin.time.Clock

interface UpdatePlaylistUseCase {
    suspend operator fun invoke(playlist: Playlist)
}

internal class UpdatePlaylistUseCaseImpl(
    private val playlistRepository: PlaylistRepository,
    private val contentUpdater: PlaylistContentUpdater,
    private val syncStateRepository: PlaylistSyncStateRepository,
) : UpdatePlaylistUseCase {
    override suspend operator fun invoke(playlist: Playlist) {
        val existing = playlistRepository.getPlaylistById(id = playlist.id)
        val updated = existing.updatedWith(playlist)
        val shouldReloadContent =
            existing.playlistType == PlaylistType.REMOTE &&
                    existing.playlistSource != updated.playlistSource

        playlistRepository.savePlaylist(playlist = updated)

        if (shouldReloadContent) {
            val contentReplaced = contentUpdater.replacePlaylistContent(
                playlist = updated,
                deleteBeforeLoad = true,
            )
            if (contentReplaced) {
                syncStateRepository.markChannelsEpgInfoUpdateRequired()
                playlistRepository.savePlaylist(
                    playlist = updated.copy(
                        lastUpdateDate = Clock.System.now().toEpochMilliseconds(),
                    ),
                )
            }
        }
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
