package com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase

import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import com.mvproject.tinyiptvkmp.core.datastore.preferences.AppPreferencesProto
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannelSource
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannelSourceType
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelFavoriteRepository
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.PlaylistChannelRepository
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.PlaylistType
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistRepository
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.util.playlistUpdatePeriodToDuration
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import kotlin.time.Clock

interface UpdateRemotePlaylistChannelsUseCase {
    suspend operator fun invoke()
}

internal class UpdateRemotePlaylistChannelsUseCaseImpl(
    private val preferencesStore: ProtoStore<AppPreferencesProto>,
    private val playlistChannelRepository: PlaylistChannelRepository,
    private val channelFavoriteRepository: ChannelFavoriteRepository,
    private val playlistRepository: PlaylistRepository,
) : UpdateRemotePlaylistChannelsUseCase,
    KoinComponent {
    private val logger by injectLogger()

    override suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            var isRefreshEpgIdRequired = false

            val currentDate = Clock.System.now().toEpochMilliseconds()
            val remote =
                playlistRepository
                    .getAllPlaylists()
                    .filter { playlist -> playlist.playlistType == PlaylistType.REMOTE }

            val playlistUpdates =
                buildList {
                    remote.forEach { playlist ->
                        val updateDuration =
                            playlistUpdatePeriodToDuration(playlist.updatePeriod.toInt())
                        val isUpdateSet = updateDuration > 0L
                        val isRequiredUpdate =
                            currentDate - playlist.lastUpdateDate > updateDuration
                        val isUpdateAllowed = isUpdateSet && isRequiredUpdate

                        logger.w { "testing remotePlaylists ${playlist.playlistName} isUpdateAllowed $isUpdateAllowed" }
                        if (isUpdateAllowed) {
                            add(playlist)
                        }
                    }
                }

            playlistUpdates.forEach { playlist ->
                val channels =
                    playlistChannelRepository.loadPlaylistChannels(playlist.toChannelSource())

                val favoriteUrls = channelFavoriteRepository
                    .loadFavoriteChannelUrls(playlistId = playlist.id)

                playlistChannelRepository.savePlaylistChannels(channels)

                channels.forEach { channel ->
                    if (channel.channelUrl in favoriteUrls) {
                        logger.w { "update in favorite ${channel.channelName}" }
                        channelFavoriteRepository.updateFavoriteChannel(
                            channelName = channel.channelName,
                            channelUrl = channel.channelUrl
                        )
                    }
                }

                playlistRepository.savePlaylist(
                    playlist = playlist.copy(lastUpdateDate = currentDate),
                )

                isRefreshEpgIdRequired = true

                logger.w { "update channels finished" }
            }

            preferencesStore.update { preferences ->
                preferences.copy(channelsEpgInfoUpdateRequired = isRefreshEpgIdRequired)
            }
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
