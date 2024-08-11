/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 06.05.24, 15:54
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.model.playlist.Playlist
import com.mvproject.tinyiptvkmp.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository

class GetDefaultPlaylistUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val playlistsRepository: PlaylistsRepository,
) {
    suspend operator fun invoke(): Playlist {
        val currentId = preferenceRepository.loadCurrentPlaylistId()
        return playlistsRepository.getPlaylistByIdRoom(id = currentId)
    }
}
