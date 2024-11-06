/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.07.24, 18:34
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.enums.PlaylistType
import com.mvproject.tinyiptvkmp.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.data.repository.RemotePlaylistRepository
import com.mvproject.tinyiptvkmp.utils.AppConstants
import com.mvproject.tinyiptvkmp.utils.KLog
import com.mvproject.tinyiptvkmp.utils.TimeUtils
import com.mvproject.tinyiptvkmp.utils.TimeUtils.typeToDuration
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
                        val updateDuration = typeToDuration(playlist.updatePeriod.toInt())
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

                val favorites =
                    favoriteChannelsRepository.loadFavoriteChannelById(playlist.id)

                playlistChannelsRepository.savePlaylistChannels(channels)

                channels.forEach { channel ->
                    val favoritesUrls = favorites.map { it.url }

                    if (channel.channelUrl in favoritesUrls) {
                        KLog.w("update in favorite ${channel.channelName}")
                        favoriteChannelsRepository.updatePlaylistFavoriteChannels(channel = channel)
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
