/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 06.05.24, 20:00
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.datasource.LocalPlaylistDataSource
import com.mvproject.tinyiptvkmp.data.datasource.RemotePlaylistDataSource
import com.mvproject.tinyiptvkmp.data.enums.PlaylistType
import com.mvproject.tinyiptvkmp.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.utils.AppConstants.LONG_NO_VALUE
import com.mvproject.tinyiptvkmp.utils.KLog

class SavePlaylistContentUseCase(
    private val localPlaylistDataSource: LocalPlaylistDataSource,
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val preferenceRepository: PreferenceRepository,
    private val remotePlaylistDataSource: RemotePlaylistDataSource,
    private val playlistsRepository: PlaylistsRepository,
) {
    suspend operator fun invoke(playlistId: Long) {
        KLog.d("testing SavePlaylistContentUseCase playlistId $playlistId")
        val playlist = playlistsRepository.getPlaylistById(id = playlistId)
        KLog.d("testing SavePlaylistContentUseCase playlist $playlist")
        val channels =
            when (playlist.playlistType) {
                PlaylistType.LOCAL ->
                    localPlaylistDataSource.getFromLocalPlaylist(
                        playlistId = playlistId,
                        source = playlist.playlistSource,
                    )

                PlaylistType.REMOTE ->
                    remotePlaylistDataSource.getFromRemotePlaylist(
                        playlistId = playlistId,
                        url = playlist.playlistSource,
                    )
            }

        if (channels.isEmpty()) {
            KLog.e("SavePlaylistContentUseCase channels is empty")
            return
        }

        playlistChannelsRepository.addPlaylistChannels(channels = channels)

        preferenceRepository.setIdForPlaylistContentLoad(id = LONG_NO_VALUE)
        preferenceRepository.setChannelsEpgInfoUpdateRequired(state = true)
    }
}
