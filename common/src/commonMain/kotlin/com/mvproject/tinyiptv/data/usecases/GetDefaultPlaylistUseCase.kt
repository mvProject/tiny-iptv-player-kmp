/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 03.09.23, 21:48
 *
 */

package com.mvproject.tinyiptv.data.usecases

import com.mvproject.tinyiptv.data.model.playlist.Playlist
import com.mvproject.tinyiptv.data.repository.PlaylistsRepository
import com.mvproject.tinyiptv.data.repository.PreferenceRepository

class GetDefaultPlaylistUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val playlistsRepository: PlaylistsRepository
) {
    suspend operator fun invoke(): Playlist {
        val currentId = preferenceRepository.loadCurrentPlaylistId()
        return playlistsRepository.getPlaylistById(id = currentId) ?: Playlist()
    }
}