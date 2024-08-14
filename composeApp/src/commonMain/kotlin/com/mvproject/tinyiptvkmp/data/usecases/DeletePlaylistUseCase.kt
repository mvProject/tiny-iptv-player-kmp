/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 06.05.24, 15:54
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.model.playlist.Playlist
import com.mvproject.tinyiptvkmp.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.utils.AppConstants.LONG_NO_VALUE
import kotlinx.coroutines.flow.first

class DeletePlaylistUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val playlistsRepository: PlaylistsRepository,
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val favoriteChannelsRepository: FavoriteChannelsRepository,
) {
    suspend operator fun invoke(playlist: Playlist) {
        val currentPlaylistId = preferenceRepository.currentPlaylistId.first()

        if (currentPlaylistId == playlist.id) {
            val availablePlaylistIds =
                playlistsRepository
                    .getAllPlaylists()
                    .map { it.id }

            val newPlaylistId =
                availablePlaylistIds
                    .firstOrNull { it != currentPlaylistId } ?: LONG_NO_VALUE

            preferenceRepository.setCurrentPlaylistId(
                playlistId = newPlaylistId,
            )
        }

        favoriteChannelsRepository.deletePlaylistFavoriteChannels(
            listId = playlist.id,
        )

        playlistChannelsRepository.deletePlaylistChannels(
            listId = playlist.id,
        )

        playlistsRepository.deletePlaylistById(id = playlist.id)
    }
}
