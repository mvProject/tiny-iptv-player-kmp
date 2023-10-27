/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 27.10.23, 17:12
 *
 */

package com.mvproject.tinyiptv.data.usecases


import com.mvproject.tinyiptv.data.datasource.LocalPlaylistDataSource
import com.mvproject.tinyiptv.data.model.playlist.Playlist
import com.mvproject.tinyiptv.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptv.data.repository.PlaylistsRepository
import com.mvproject.tinyiptv.data.repository.PreferenceRepository
import com.mvproject.tinyiptv.utils.AppConstants
import io.github.aakira.napier.Napier

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
        val channels = localPlaylistDataSource.getFromLocalPlaylist(
            playlistId = playlist.id,

            uri = source
        )

        playlistChannelsRepository.addPlaylistChannels(channels = channels)

        playlistsRepository.addPlaylist(playlist = playlist)

        if (playlistsRepository.playlistCount == AppConstants.INT_VALUE_1) {
            Napier.w("testing need set as current")
            preferenceRepository.setCurrentPlaylistId(playlistId = playlist.id)
        }

        preferenceRepository.setChannelsEpgInfoUpdateRequired(state = true)
    }
}