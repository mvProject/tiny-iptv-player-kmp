package com.mvproject.tinyiptvkmp.core.domain.usecase

import com.mvproject.tinyiptvkmp.core.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.core.data.repository.RemotePlaylistRepository
import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.core.domain.mappers.EntityMapper.toFavType
import com.mvproject.tinyiptvkmp.data.enums.PlaylistType
import com.mvproject.tinyiptvkmp.utils.AppConstants
import com.mvproject.tinyiptvkmp.utils.KLog
import com.mvproject.tinyiptvkmp.utils.TimeUtils
import kotlinx.coroutines.Dispatchers
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

                        KLog.w("testing remotePlaylists ${playlist.playlistName} isUpdateAllowed $isUpdateAllowed")
                        if (isUpdateAllowed) {
                            add(playlist)
                        }
                    }
                }

            playlistUpdates.forEach { playlist ->
                val channels =
                    remotePlaylistRepository.getFromRemotePlaylist(
                        playlistId = playlist.id,
                        url = playlist.playlistSource,
                    )

                val favorites = favoriteChannelsRepository
                    .loadFavoriteChannelById(playlist.id)
                    .map { item -> item.toFavType() }

                playlistChannelsRepository.savePlaylistChannels(channels)

                channels.forEach { channel ->
                    val favoritesUrls = favorites.map { it.url }

                    if (channel.channelUrl in favoritesUrls) {
                        KLog.w("update in favorite ${channel.channelName}")
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

                KLog.w("update channels finished")
            }

            preferenceRepository.setChannelsEpgInfoUpdateRequired(state = isRefreshEpgIdRequired)
        }
    }
}