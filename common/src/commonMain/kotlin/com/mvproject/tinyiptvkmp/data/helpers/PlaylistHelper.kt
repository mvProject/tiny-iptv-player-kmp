/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.helpers

import com.mvproject.tinyiptvkmp.data.model.playlist.Playlist
import com.mvproject.tinyiptvkmp.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository

class PlaylistHelper(
    private val preferenceRepository: PreferenceRepository,
    private val playlistsRepository: PlaylistsRepository
) {

    val currentPlaylistId
        get() = preferenceRepository.currentPlaylistId

    fun loadPlaylists(): List<Playlist> {
        return playlistsRepository.getAllPlaylists()
    }

    val allPlaylistsFlow
        get() = playlistsRepository.allPlaylistsFlow()

    suspend fun setCurrentPlaylist(playlistId: Long) {
        preferenceRepository.setCurrentPlaylistId(playlistId)
    }
}