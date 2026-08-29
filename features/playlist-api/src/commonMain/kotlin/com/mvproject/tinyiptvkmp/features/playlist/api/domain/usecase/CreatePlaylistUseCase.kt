package com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.PlaylistType
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistRepository
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistSyncStateRepository
import kotlin.time.Clock

interface CreatePlaylistUseCase {
    suspend operator fun invoke(playlist: Playlist)
}

internal class CreatePlaylistUseCaseImpl(
    private val playlistRepository: PlaylistRepository,
    private val contentUpdater: PlaylistContentUpdater,
    private val syncStateRepository: PlaylistSyncStateRepository,
) : CreatePlaylistUseCase {
    override suspend operator fun invoke(playlist: Playlist) {
        val savedPlaylist =
            playlist.copy(
                isSelected = playlistRepository.getAllPlaylists().isEmpty(),
            )

        playlistRepository.savePlaylist(playlist = savedPlaylist)
        val contentReplaced = contentUpdater.replacePlaylistContent(
            playlist = savedPlaylist,
            deleteBeforeLoad = true,
        )

        if (contentReplaced) {
            syncStateRepository.markChannelsEpgInfoUpdateRequired()
        }

        if (contentReplaced && savedPlaylist.playlistType == PlaylistType.REMOTE) {
            playlistRepository.savePlaylist(
                playlist = savedPlaylist.copy(
                    lastUpdateDate = Clock.System.now().toEpochMilliseconds(),
                ),
            )
        }
    }
}
