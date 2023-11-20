/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.model.playlist.Playlist
import com.mvproject.tinyiptvkmp.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.utils.KLog

class GetRemotePlaylistsUseCase(
    private val playlistsRepository: PlaylistsRepository
) {
    suspend operator fun invoke(): List<Playlist> {
        val remotePlaylists = playlistsRepository.getAllPlaylists()
            .filter { playlist ->
                !playlist.isLocalSource
            }
        KLog.w("testing remotePlaylists count:${remotePlaylists.count()}")
        return remotePlaylists
    }
}