/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 03.09.23, 21:48
 *
 */

package com.mvproject.tinyiptv.data.usecases

import com.mvproject.tinyiptv.data.datasource.RemotePlaylistDataSource
import com.mvproject.tinyiptv.data.model.playlist.Playlist
import com.mvproject.tinyiptv.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptv.data.repository.PlaylistsRepository
import com.mvproject.tinyiptv.data.repository.PreferenceRepository
import com.mvproject.tinyiptv.utils.AppConstants
import com.mvproject.tinyiptv.utils.TimeUtils.actualDate
import io.github.aakira.napier.Napier

class AddRemotePlaylistUseCase(
    private val remotePlaylistDataSource: RemotePlaylistDataSource,
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val preferenceRepository: PreferenceRepository,
    private val playlistsRepository: PlaylistsRepository
) {
    suspend operator fun invoke(
        playlist: Playlist
    ) {
        val channels = remotePlaylistDataSource.getFromRemotePlaylist(
            playlistId = playlist.id,
            url = playlist.playlistUrl
        )
        playlistChannelsRepository.addPlaylistChannels(channels = channels)


        playlistsRepository.addPlaylist(
            playlist = playlist.copy(lastUpdateDate = actualDate)
        )

        if (playlistsRepository.playlistCount == AppConstants.INT_VALUE_1) {
            Napier.w("testing need set as current")
            preferenceRepository.setCurrentPlaylistId(playlistId = playlist.id)
        }

        preferenceRepository.setChannelsEpgInfoUpdateRequired(state = true)
    }
}