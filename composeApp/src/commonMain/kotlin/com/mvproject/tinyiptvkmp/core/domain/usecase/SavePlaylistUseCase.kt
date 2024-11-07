package com.mvproject.tinyiptvkmp.core.domain.usecase

import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.core.domain.model.Playlist
import com.mvproject.tinyiptvkmp.data.enums.PlaylistType

class SavePlaylistUseCase(
    private val preferenceRepository: PreferenceRepository,
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
            preferenceRepository.setIdForPlaylistContentLoad(id = playlist.id)
        }
    }
}