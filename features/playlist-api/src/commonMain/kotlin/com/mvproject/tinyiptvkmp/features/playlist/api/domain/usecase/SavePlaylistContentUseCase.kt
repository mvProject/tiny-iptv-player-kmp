package com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.PlaylistType
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistRepository
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistSyncStateRepository
import kotlin.time.Clock

interface SavePlaylistContentUseCase {
    suspend operator fun invoke(playlistId: String)
}

internal class SavePlaylistContentUseCaseImpl(
    private val playlistRepository: PlaylistRepository,
    private val contentUpdater: PlaylistContentUpdater,
    private val syncStateRepository: PlaylistSyncStateRepository,
) : SavePlaylistContentUseCase {
    override suspend operator fun invoke(playlistId: String) {
        val playlist = playlistRepository.getPlaylistById(id = playlistId)
        val contentReplaced = contentUpdater.replacePlaylistContent(
            playlist = playlist,
            deleteBeforeLoad = false,
        )
        if (!contentReplaced) return

        syncStateRepository.markChannelsEpgInfoUpdateRequired()

        if (playlist.playlistType == PlaylistType.REMOTE) {
            playlistRepository.savePlaylist(
                playlist = playlist.copy(
                    lastUpdateDate = Clock.System.now().toEpochMilliseconds(),
                ),
            )
        }
    }
}
