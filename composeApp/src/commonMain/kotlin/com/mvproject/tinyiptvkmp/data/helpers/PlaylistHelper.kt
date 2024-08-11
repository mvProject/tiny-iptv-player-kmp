/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:29
 *
 */

package com.mvproject.tinyiptvkmp.data.helpers

import com.mvproject.tinyiptvkmp.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository

class PlaylistHelper(
    private val preferenceRepository: PreferenceRepository,
    private val playlistsRepository: PlaylistsRepository,
) {
    val currentPlaylistId
        get() = preferenceRepository.currentPlaylistId

    /*    fun loadPlaylists(): List<Playlist> {
            return playlistsRepository.getAllPlaylistsRoom()
        }*/

    fun allPlaylistsFlow() = playlistsRepository.allPlaylistsFlowRoom()

    suspend fun setCurrentPlaylist(playlistId: Long) {
        preferenceRepository.setCurrentPlaylistId(playlistId)
    }
}
