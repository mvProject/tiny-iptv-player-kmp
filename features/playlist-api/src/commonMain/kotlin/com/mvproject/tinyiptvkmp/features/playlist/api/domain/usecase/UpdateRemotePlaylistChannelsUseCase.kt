package com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistRepository
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistSyncStateRepository
import kotlin.time.Clock

interface UpdateRemotePlaylistChannelsUseCase {
    suspend operator fun invoke()
}

internal class UpdateRemotePlaylistChannelsUseCaseImpl(
    private val playlistRepository: PlaylistRepository,
    private val contentUpdater: PlaylistContentUpdater,
    private val syncStateRepository: PlaylistSyncStateRepository,
) : UpdateRemotePlaylistChannelsUseCase {
    override suspend operator fun invoke() {
        val currentDate = Clock.System.now().toEpochMilliseconds()
        var isEpgInfoUpdateRequired = false

        playlistRepository.getDueRemotePlaylists(nowMillis = currentDate).forEach { playlist ->
            val contentReplaced = contentUpdater.replacePlaylistContent(
                playlist = playlist,
                deleteBeforeLoad = false,
            )
            if (contentReplaced) {
                playlistRepository.savePlaylist(
                    playlist = playlist.copy(lastUpdateDate = currentDate),
                )
                isEpgInfoUpdateRequired = true
            }
        }

        if (isEpgInfoUpdateRequired) {
            syncStateRepository.markChannelsEpgInfoUpdateRequired()
        }
    }
}
