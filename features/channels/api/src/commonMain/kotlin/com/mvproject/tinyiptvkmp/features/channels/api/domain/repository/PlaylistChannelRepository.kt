package com.mvproject.tinyiptvkmp.features.channels.api.domain.repository

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannel

interface PlaylistChannelRepository {
    suspend fun savePlaylistChannels(channels: List<PlaylistChannel>)

    suspend fun loadLocalPlaylistChannels(playlistId: String, source: String): List<PlaylistChannel>

    suspend fun loadRemotePlaylistChannels(
        playlistId: String,
        source: String
    ): List<PlaylistChannel>

    suspend fun loadPlaylistGroups(playlistId: String): List<String>

    suspend fun loadPlaylistGroupCounts(playlistId: String): Map<String, Int>

    suspend fun loadPlaylistChannelsCount(playlistId: String): Int

    suspend fun loadPlaylistGroupChannelsCount(playlistId: String, group: String): Int

    suspend fun loadChannelsById(playlistId: String): List<PlaylistChannel>

    suspend fun loadAllChannels(): List<PlaylistChannel>

    suspend fun loadPlaylistChannelsByUrls(
        playlistId: String,
        urls: List<String>
    ): List<PlaylistChannel>

    suspend fun loadPlaylistGroupChannels(playlistId: String, group: String): List<PlaylistChannel>

    suspend fun deletePlaylistChannels(listId: String)
}
