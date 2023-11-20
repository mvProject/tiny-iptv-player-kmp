/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 03.09.23, 21:48
 *
 */

package com.mvproject.tinyiptv.data.usecases

import com.mvproject.tinyiptv.data.model.playlist.Playlist
import com.mvproject.tinyiptv.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptv.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptv.data.repository.PlaylistsRepository
import com.mvproject.tinyiptv.data.repository.PreferenceRepository
import com.mvproject.tinyiptv.utils.AppConstants
import kotlinx.coroutines.flow.first

class DeletePlaylistUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val playlistsRepository: PlaylistsRepository,
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val favoriteChannelsRepository: FavoriteChannelsRepository,
) {
    suspend operator fun invoke(
        playlist: Playlist
    ) {
        val currentPlaylistId = preferenceRepository.currentPlaylistId.first()

        if (currentPlaylistId == playlist.id) {
            val availablePlaylistIds = playlistsRepository
                .getAllPlaylists()
                .map { it.id }

            val newPlaylistId = availablePlaylistIds
                .firstOrNull { it != currentPlaylistId } ?: AppConstants.LONG_NO_VALUE

            preferenceRepository.setCurrentPlaylistId(
                playlistId = newPlaylistId
            )
        }

        favoriteChannelsRepository.deletePlaylistFavoriteChannels(
            listId = playlist.id
        )

        playlistChannelsRepository.deletePlaylistChannels(
            listId = playlist.id
        )

        playlistsRepository.deletePlaylistById(
            id = playlist.id
        )
    }
}