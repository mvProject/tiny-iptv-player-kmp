/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 03.09.23, 21:48
 *
 */

package com.mvproject.tinyiptv.data.usecases

import com.mvproject.tinyiptv.data.models.playlist.Playlist
import com.mvproject.tinyiptv.data.repository.PlaylistsRepository
import com.mvproject.tinyiptv.utils.AppConstants.LONG_VALUE_ZERO

class GetPlaylistUseCase(
    private val playlistsRepository: PlaylistsRepository
) {
    suspend operator fun invoke(
        playlistId: String
    ): Playlist {
        val id = playlistId.toLongOrNull() ?: return Playlist(id = LONG_VALUE_ZERO)
        return playlistsRepository.getPlaylistById(id = id) ?: Playlist(id = LONG_VALUE_ZERO)
    }
}