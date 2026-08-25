package com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase

import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import com.mvproject.tinyiptvkmp.core.datastore.preferences.AppPreferencesProto
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.PlaylistType
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistRepository

interface SavePlaylistUseCase {
    suspend operator fun invoke(
        playlist: Playlist,
        isUpdate: Boolean,
    )
}

internal class SavePlaylistUseCaseImpl(
    private val preferencesStore: ProtoStore<AppPreferencesProto>,
    private val playlistRepository: PlaylistRepository,
) : SavePlaylistUseCase {
    override suspend operator fun invoke(
        playlist: Playlist,
        isUpdate: Boolean,
    ) {
        val playlists = playlistRepository.getAllPlaylists()
        val list = playlist.copy(isSelected = playlists.isEmpty())
        playlistRepository.savePlaylist(playlist = list)

        if (!isUpdate || playlist.playlistType == PlaylistType.REMOTE) {
            preferencesStore.update { preferences ->
                preferences.copy(playlistContentLoadRequired = playlist.id)
            }
        }
    }
}
