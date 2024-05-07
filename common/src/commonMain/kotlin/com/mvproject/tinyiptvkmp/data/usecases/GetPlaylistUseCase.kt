/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 06.05.24, 15:31
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.model.playlist.Playlist
import com.mvproject.tinyiptvkmp.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.utils.AppConstants.LONG_VALUE_ZERO

class GetPlaylistUseCase(
    private val playlistsRepository: PlaylistsRepository,
) {
    suspend operator fun invoke(playlistId: String): Playlist {
        val id = playlistId.toLongOrNull() ?: return Playlist(id = LONG_VALUE_ZERO)
        //  return playlistsRepository.getPlaylistById(id = id) ?: Playlist(id = LONG_VALUE_ZERO)
        return playlistsRepository.getPlaylistByIdRoom(id = id) ?: Playlist(id = LONG_VALUE_ZERO)
    }
}
