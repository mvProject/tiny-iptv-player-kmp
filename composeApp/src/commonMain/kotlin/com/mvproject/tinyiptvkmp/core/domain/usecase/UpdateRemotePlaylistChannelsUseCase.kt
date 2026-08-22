package com.mvproject.tinyiptvkmp.core.domain.usecase

import com.mvproject.tinyiptvkmp.core.common.LONG_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.common.utils.actualDate
import com.mvproject.tinyiptvkmp.core.common.utils.typeToDuration
import com.mvproject.tinyiptvkmp.core.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.core.data.repository.RemotePlaylistRepository
import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.core.domain.enums.PlaylistType
import com.mvproject.tinyiptvkmp.core.domain.mappers.Mapper.toFavType
import com.mvproject.tinyiptvkmp.core.domain.mappers.Mapper.toPlaylistChannel
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

class UpdateRemotePlaylistChannelsUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val remotePlaylistRepository: RemotePlaylistRepository,
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val favoriteChannelsRepository: FavoriteChannelsRepository,
    private val playlistsRepository: PlaylistsRepository,
) : KoinComponent {
    private val logger by injectLogger()

    suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            var isRefreshEpgIdRequired = false

            val currentDate = actualDate
            val remote =
                playlistsRepository
                    .getAllPlaylists()
                    .filter { playlist -> playlist.playlistType == PlaylistType.REMOTE }

            val playlistUpdates =
                buildList {
                    remote.forEach { playlist ->
                        val updateDuration = typeToDuration(playlist.updatePeriod.toInt())
                        val isUpdateSet = updateDuration > LONG_VALUE_ZERO
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
                val channels = remotePlaylistRepository
                    .getFromRemotePlaylist(url = playlist.playlistSource)
                    .map { it.toPlaylistChannel(id = playlist.id) }

                val favorites = favoriteChannelsRepository
                    .loadFavoriteChannelById(playlist.id)
                    .map { item -> item.toFavType() }

                playlistChannelsRepository.savePlaylistChannels(channels)

                channels.forEach { channel ->
                    val favoritesUrls = favorites.map { it.url }

                    if (channel.channelUrl in favoritesUrls) {
                        logger.w { "update in favorite ${channel.channelName}" }
                        favoriteChannelsRepository.updatePlaylistFavoriteChannels(
                            channelName = channel.channelName,
                            channelUrl = channel.channelUrl
                        )
                    }
                }

                playlistsRepository.savePlaylist(
                    playlist = playlist.copy(lastUpdateDate = currentDate),
                )

                isRefreshEpgIdRequired = true

                logger.w { "update channels finished" }
            }

            preferenceRepository.setChannelsEpgInfoUpdateRequired(state = isRefreshEpgIdRequired)
        }
    }
}
