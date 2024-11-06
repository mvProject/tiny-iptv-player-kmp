/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 06.05.24, 20:00
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.enums.PlaylistType
import com.mvproject.tinyiptvkmp.data.repository.LocalPlaylistRepository
import com.mvproject.tinyiptvkmp.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.data.repository.RemotePlaylistRepository
import com.mvproject.tinyiptvkmp.utils.AppConstants.LONG_NO_VALUE
import com.mvproject.tinyiptvkmp.utils.KLog

class SavePlaylistContentUseCase(
    private val localPlaylistRepository: LocalPlaylistRepository,
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val preferenceRepository: PreferenceRepository,
    private val remotePlaylistRepository: RemotePlaylistRepository,
    private val playlistsRepository: PlaylistsRepository,
) {
    suspend operator fun invoke(playlistId: Long) {
        val playlist = playlistsRepository.getPlaylistById(id = playlistId)

        val channels =
            when (playlist.playlistType) {
                PlaylistType.LOCAL ->
                    localPlaylistRepository.getFromLocalPlaylist(
                        playlistId = playlistId,
                        source = playlist.playlistSource,
                    )

                PlaylistType.REMOTE ->
                    remotePlaylistRepository.getFromRemotePlaylist(
                        playlistId = playlistId,
                        url = playlist.playlistSource,
                    )
            }

        if (channels.isEmpty()) {
            KLog.e("SavePlaylistContentUseCase channels is empty")
            return
        }

        playlistChannelsRepository.savePlaylistChannels(channels = channels)

        preferenceRepository.setIdForPlaylistContentLoad(id = LONG_NO_VALUE)
        preferenceRepository.setChannelsEpgInfoUpdateRequired(state = true)
    }
}
