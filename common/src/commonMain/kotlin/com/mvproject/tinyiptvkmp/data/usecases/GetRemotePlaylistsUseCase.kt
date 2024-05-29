/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 06.05.24, 15:54
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.model.playlist.Playlist
import com.mvproject.tinyiptvkmp.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.utils.KLog

class GetRemotePlaylistsUseCase(
    private val playlistsRepository: PlaylistsRepository,
) {
    suspend operator fun invoke(): List<Playlist> {
        val remotePlaylists =
            playlistsRepository.getAllPlaylistsRoom()
                .filter { playlist ->
                    !playlist.isLocalSource
                }
        KLog.w("remotePlaylists count:${remotePlaylists.count()}")
        return remotePlaylists
    }
}
