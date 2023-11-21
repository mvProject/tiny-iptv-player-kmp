/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.datasource.RemotePlaylistDataSource
import com.mvproject.tinyiptvkmp.data.model.playlist.Playlist
import com.mvproject.tinyiptvkmp.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.utils.KLog
import com.mvproject.tinyiptvkmp.utils.TimeUtils

class UpdateRemotePlaylistChannelsUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val remotePlaylistDataSource: RemotePlaylistDataSource,
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val favoriteChannelsRepository: FavoriteChannelsRepository,
    private val playlistsRepository: PlaylistsRepository
) {
    suspend operator fun invoke(
        playlist: Playlist
    ) {
        KLog.w("update channels for playlist:${playlist.playlistTitle}")

        val channels = remotePlaylistDataSource.getFromRemotePlaylist(
            playlistId = playlist.id,
            url = playlist.playlistUrl
        )
        val favorites = favoriteChannelsRepository
            .loadPlaylistFavoriteChannelUrls(listId = playlist.id)

        playlistChannelsRepository.updatePlaylistChannels(channels)

        channels.forEach { channel ->
            if (channel.channelUrl in favorites) {
                KLog.w("update in favorite ${channel.channelName}")
                favoriteChannelsRepository.updatePlaylistFavoriteChannels(channel = channel)
            }
        }

        playlistsRepository.updatePlaylist(
            playlist = playlist.copy(lastUpdateDate = TimeUtils.actualDate)
        )

        preferenceRepository.setChannelsEpgInfoUpdateRequired(state = true)

        KLog.w("update channels finished")
    }
}