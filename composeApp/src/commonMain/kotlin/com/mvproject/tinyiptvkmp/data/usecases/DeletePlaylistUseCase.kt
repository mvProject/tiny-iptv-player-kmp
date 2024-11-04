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

class DeletePlaylistUseCase(
    private val playlistsRepository: PlaylistsRepository,
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val favoriteChannelsRepository: FavoriteChannelsRepository,
) {
    suspend operator fun invoke(playlist: Playlist) {
        if (playlist.isSelected) {
            // Find another list to be set as selected
            val updateSelected =
                playlistsRepository
                    .getAllPlaylists()
                    .firstOrNull { !it.isSelected }
            // Update the selection state of the new list if found
            updateSelected?.let { selected ->
                playlistsRepository.savePlaylist(selected.copy(isSelected = true))
            }
        }
        playlistsRepository.deleteSinglePlaylist(playlist = playlist)

        favoriteChannelsRepository.deletePlaylistFavoriteChannels(
            listId = playlist.id,
        )

        playlistChannelsRepository.deletePlaylistChannels(
            listId = playlist.id,
        )
    }
}
