package com.mvproject.tinyiptvkmp.features.channels.api.domain.repository

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannelSource

interface PlaylistChannelRepository {
    suspend fun savePlaylistChannels(channels: List<PlaylistChannel>)

    suspend fun loadPlaylistChannels(source: PlaylistChannelSource): List<PlaylistChannel>

    suspend fun loadPlaylistGroups(): List<String>

    suspend fun loadPlaylistChannelsCount(): Int

    suspend fun loadPlaylistGroupChannelsCount(group: String): Int

    suspend fun loadChannelsById(): List<PlaylistChannel>

    suspend fun loadAllChannels(): List<PlaylistChannel>

    suspend fun loadPlaylistChannelsByUrls(urls: List<String>): List<PlaylistChannel>

    suspend fun loadPlaylistGroupChannels(group: String): List<PlaylistChannel>

    suspend fun deletePlaylistChannels(listId: String)
}
