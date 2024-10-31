/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 06.05.24, 20:00
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.model.playlist.Playlist
import com.mvproject.tinyiptvkmp.data.repository.PlaylistsRepository

class SelectPlaylistUseCase(
    private val playlistsRepository: PlaylistsRepository,
) {
    suspend operator fun invoke(playlist: Playlist) {
        val playlists = playlistsRepository.getAllPlaylists()
        val changedPlaylists =
            playlists.map { it.copy(isSelected = it.id == playlist.id) }
        playlistsRepository.savePlaylists(playlists = changedPlaylists)
    }
}
