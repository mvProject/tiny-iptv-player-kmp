/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases


import com.mvproject.tinyiptvkmp.data.model.playlist.Playlist
import com.mvproject.tinyiptvkmp.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.utils.AppConstants
import com.mvproject.tinyiptvkmp.utils.KLog

class AddLocalPlaylistUseCase(
    private val localPlaylistDataSource: com.mvproject.tinyiptvkmp.platform.LocalPlaylistDataSource,
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val preferenceRepository: PreferenceRepository,
    private val playlistsRepository: PlaylistsRepository
) {
    suspend operator fun invoke(
        playlist: Playlist,
        source: String
    ) {

        val channels = localPlaylistDataSource.getLocalPlaylistData(
            playlistId = playlist.id,
            uri = source
        )

        if (channels.isEmpty()) {
            KLog.e("AddLocalPlaylistUseCase channels is empty")
            return
        }

        playlistChannelsRepository.addPlaylistChannels(channels = channels)

        playlistsRepository.addPlaylist(playlist = playlist)

        if (playlistsRepository.playlistCount == AppConstants.INT_VALUE_1) {
            KLog.w("playlist ${playlist.playlistTitle} need set as current")
            preferenceRepository.setCurrentPlaylistId(playlistId = playlist.id)
        }

        preferenceRepository.setChannelsEpgInfoUpdateRequired(state = true)
    }
}