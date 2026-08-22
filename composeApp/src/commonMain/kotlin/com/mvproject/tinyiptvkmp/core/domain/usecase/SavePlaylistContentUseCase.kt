package com.mvproject.tinyiptvkmp.core.domain.usecase

import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.data.repository.LocalPlaylistRepository
import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.core.data.repository.RemotePlaylistRepository
import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.core.domain.enums.PlaylistType
import com.mvproject.tinyiptvkmp.core.domain.mappers.Mapper.toPlaylistChannel
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import org.koin.core.component.KoinComponent

class SavePlaylistContentUseCase(
    private val localPlaylistRepository: LocalPlaylistRepository,
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val preferenceRepository: PreferenceRepository,
    private val remotePlaylistRepository: RemotePlaylistRepository,
    private val playlistsRepository: PlaylistsRepository,
) : KoinComponent {
    private val logger by injectLogger()

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
            logger.e { "SavePlaylistContentUseCase channels is empty" }
            return
        }

        val channels = parsedChannels.map { it.toPlaylistChannel(id = playlistId) }

        playlistChannelsRepository.savePlaylistChannels(channels = channels)

        preferenceRepository.setIdForPlaylistContentLoad(id = String.empty)
        preferenceRepository.setChannelsEpgInfoUpdateRequired(state = true)
    }
}
