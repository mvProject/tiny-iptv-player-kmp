package com.mvproject.tinyiptvkmp.core.domain.usecase

import co.touchlab.kermit.Logger
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.data.repository.LocalPlaylistRepository
import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.core.data.repository.RemotePlaylistRepository
import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.core.domain.enums.PlaylistType
import com.mvproject.tinyiptvkmp.core.domain.mappers.Mapper.toPlaylistChannel

class SavePlaylistContentUseCase(
    private val localPlaylistRepository: LocalPlaylistRepository,
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val preferenceRepository: PreferenceRepository,
    private val remotePlaylistRepository: RemotePlaylistRepository,
    private val playlistsRepository: PlaylistsRepository,
) {
    suspend operator fun invoke(playlistId: String) {
        val playlist = playlistsRepository.getPlaylistById(id = playlistId)
        val parsedChannels =
            when (playlist.playlistType) {
                PlaylistType.LOCAL ->
                    localPlaylistRepository.getFromLocalPlaylist(
                        source = playlist.playlistSource
                    )

                PlaylistType.REMOTE ->
                    remotePlaylistRepository.getFromRemotePlaylist(
                        url = playlist.playlistSource,
                    )
            }
        if (parsedChannels.isEmpty()) {
            Logger.e("SavePlaylistContentUseCase channels is empty")
            return
        }

        val channels = parsedChannels.map { it.toPlaylistChannel(id = playlistId) }

        playlistChannelsRepository.savePlaylistChannels(channels = channels)

        preferenceRepository.setIdForPlaylistContentLoad(id = String.empty)
        preferenceRepository.setChannelsEpgInfoUpdateRequired(state = true)
    }
}