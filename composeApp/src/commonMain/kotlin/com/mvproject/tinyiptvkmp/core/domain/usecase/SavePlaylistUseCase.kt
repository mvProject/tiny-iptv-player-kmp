package com.mvproject.tinyiptvkmp.core.domain.usecase

import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import com.mvproject.tinyiptvkmp.core.datastore.preferences.AppPreferencesProto
import com.mvproject.tinyiptvkmp.core.domain.enums.PlaylistType
import com.mvproject.tinyiptvkmp.core.domain.model.Playlist

class SavePlaylistUseCase(
    private val preferencesStore: ProtoStore<AppPreferencesProto>,
    private val playlistsRepository: PlaylistsRepository,
) {
    suspend operator fun invoke(
        playlist: Playlist,
        isUpdate: Boolean,
    ) {
        val playlists = playlistsRepository.getAllPlaylists()
        val list = playlist.copy(isSelected = playlists.isEmpty())
        playlistsRepository.savePlaylist(playlist = list)

        if (!isUpdate || playlist.playlistType == PlaylistType.REMOTE) {
            preferencesStore.update { preferences ->
                preferences.copy(playlistContentLoadRequired = playlist.id)
            }
        }
    }
}
