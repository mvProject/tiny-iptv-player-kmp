/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.09.23, 16:29
 *
 */

package com.mvproject.tinyiptv.data.helpers

import com.mvproject.tinyiptv.data.models.playlist.Playlist
import com.mvproject.tinyiptv.data.repository.PlaylistsRepository
import com.mvproject.tinyiptv.data.repository.PreferenceRepository

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