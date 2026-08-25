package com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase

import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import com.mvproject.tinyiptvkmp.core.datastore.preferences.AppPreferencesProto
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannelSource
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannelSourceType
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.PlaylistChannelRepository
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.PlaylistType
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistRepository
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import org.koin.core.component.KoinComponent

interface SavePlaylistContentUseCase {
    suspend operator fun invoke(playlistId: String)
}

internal class SavePlaylistContentUseCaseImpl(
    private val playlistChannelRepository: PlaylistChannelRepository,
    private val preferencesStore: ProtoStore<AppPreferencesProto>,
    private val playlistRepository: PlaylistRepository,
) : SavePlaylistContentUseCase,
    KoinComponent {
    private val logger by injectLogger()

    override suspend operator fun invoke(playlistId: String) {
        val playlist = playlistRepository.getPlaylistById(id = playlistId)
        val channels = playlistChannelRepository.loadPlaylistChannels(playlist.toChannelSource())
        if (channels.isEmpty()) {
            logger.e { "SavePlaylistContentUseCase channels is empty" }
            return
        }
        playlistChannelRepository.savePlaylistChannels(channels = channels)

        preferencesStore.update { preferences ->
            preferences.copy(
                playlistContentLoadRequired = "",
                channelsEpgInfoUpdateRequired = true,
            )
        }
    }
}

private fun Playlist.toChannelSource() =
    PlaylistChannelSource(
        parentListId = id,
        source = playlistSource,
        sourceType = when (playlistType) {
            PlaylistType.LOCAL -> PlaylistChannelSourceType.LOCAL
            PlaylistType.REMOTE -> PlaylistChannelSourceType.REMOTE
        },
    )
