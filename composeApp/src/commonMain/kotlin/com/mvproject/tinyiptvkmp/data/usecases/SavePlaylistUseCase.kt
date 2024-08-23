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
import com.mvproject.tinyiptvkmp.utils.AppConstants.INT_VALUE_1
import com.mvproject.tinyiptvkmp.utils.KLog

class SavePlaylistUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val playlistsRepository: PlaylistsRepository,
) {
    suspend operator fun invoke(
        playlist: Playlist,
        isUpdate: Boolean,
    ) {
        playlistsRepository.savePlaylist(playlist = playlist)

        if (playlistsRepository.playlistCount() == INT_VALUE_1) {
            KLog.w("playlist ${playlist.playlistName} need set as current")
            preferenceRepository.setCurrentPlaylistId(playlistId = playlist.id)
        }

        if (!isUpdate || playlist.playlistType == PlaylistType.REMOTE) {
            preferenceRepository.setIdForPlaylistContentLoad(id = playlist.id)
        }
    }
}
