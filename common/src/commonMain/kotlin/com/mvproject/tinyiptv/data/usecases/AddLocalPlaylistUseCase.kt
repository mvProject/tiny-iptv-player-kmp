/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 27.10.23, 17:12
 *
 */

package com.mvproject.tinyiptv.data.usecases


import com.mvproject.tinyiptv.data.model.playlist.Playlist
import com.mvproject.tinyiptv.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptv.data.repository.PlaylistsRepository
import com.mvproject.tinyiptv.data.repository.PreferenceRepository
import com.mvproject.tinyiptv.platform.LocalPlaylistDataSource
import com.mvproject.tinyiptv.utils.AppConstants
import com.mvproject.tinyiptv.utils.KLog

class AddLocalPlaylistUseCase(
    private val localPlaylistDataSource: LocalPlaylistDataSource,
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