package com.mvproject.tinyiptvkmp.core.domain.usecase

import co.touchlab.kermit.Logger
import com.mvproject.tinyiptvkmp.core.common.AppConstants
import com.mvproject.tinyiptvkmp.core.common.utils.TimeUtils
import com.mvproject.tinyiptvkmp.core.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.core.data.repository.RemotePlaylistRepository
import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.core.domain.enums.PlaylistType
import com.mvproject.tinyiptvkmp.core.domain.mappers.Mapper.toFavType
import com.mvproject.tinyiptvkmp.core.domain.mappers.Mapper.toPlaylistChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class UpdateRemotePlaylistChannelsUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val remotePlaylistRepository: RemotePlaylistRepository,
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val favoriteChannelsRepository: FavoriteChannelsRepository,
    private val playlistsRepository: PlaylistsRepository,
) {
    suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            var isRefreshEpgIdRequired = false

            val currentDate = TimeUtils.actualDate
            val remote =
                playlistsRepository
                    .getAllPlaylists()
                    .filter { playlist -> playlist.playlistType == PlaylistType.REMOTE }

            val playlistUpdates =
                buildList {
                    remote.forEach { playlist ->
                        val updateDuration = TimeUtils.typeToDuration(playlist.updatePeriod.toInt())
                        val isUpdateSet = updateDuration > AppConstants.LONG_VALUE_ZERO
                        val isRequiredUpdate =
                            currentDate - playlist.lastUpdateDate > updateDuration
                        val isUpdateAllowed = isUpdateSet && isRequiredUpdate

                        Logger.w("testing remotePlaylists ${playlist.playlistName} isUpdateAllowed $isUpdateAllowed")
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
                        Logger.w("update in favorite ${channel.channelName}")
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

                Logger.w("update channels finished")
            }

            preferenceRepository.setChannelsEpgInfoUpdateRequired(state = isRefreshEpgIdRequired)
        }
    }
}