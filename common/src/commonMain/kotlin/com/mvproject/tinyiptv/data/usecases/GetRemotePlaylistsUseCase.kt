/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 03.09.23, 21:48
 *
 */

package com.mvproject.tinyiptv.data.usecases

import com.mvproject.tinyiptv.data.model.playlist.Playlist
import com.mvproject.tinyiptv.data.repository.PlaylistsRepository
import io.github.aakira.napier.Napier

class GetRemotePlaylistsUseCase(
    private val playlistsRepository: PlaylistsRepository
) {
    suspend operator fun invoke(): List<Playlist> {
        val remotePlaylists = playlistsRepository.getAllPlaylists()
            .filter { playlist ->
                !playlist.isLocalSource
            }
        Napier.w("testing remotePlaylists count:${remotePlaylists.count()}")
        return remotePlaylists
    }
}