/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 06.05.24, 20:00
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.enums.PlaylistType
import com.mvproject.tinyiptvkmp.data.model.playlist.Playlist
import com.mvproject.tinyiptvkmp.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository

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
