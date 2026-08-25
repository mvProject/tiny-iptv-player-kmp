package com.mvproject.tinyiptvkmp.features.channels.api.data.local

import com.mvproject.tinyiptvkmp.features.channels.api.data.local.database.PlaylistChannelEntity

internal interface PlaylistChannelLocalDataSource {
    suspend fun savePlaylistChannels(channels: List<PlaylistChannelEntity>)

    suspend fun loadPlaylistGroups(playlistId: String): List<String>

    suspend fun loadPlaylistChannelsCount(playlistId: String): Int

    suspend fun loadPlaylistGroupChannelsCount(
        playlistId: String,
        group: String,
    ): Int

    suspend fun loadChannelsById(playlistId: String): List<PlaylistChannelEntity>

    suspend fun loadAllChannels(): List<PlaylistChannelEntity>

    suspend fun loadPlaylistChannelsByUrls(
        playlistId: String,
        urls: List<String>,
    ): List<PlaylistChannelEntity>

    suspend fun loadPlaylistGroupChannels(
        playlistId: String,
        group: String,
    ): List<PlaylistChannelEntity>

    suspend fun deletePlaylistChannels(id: String)

    suspend fun loadPlaylistContent(source: String): List<PlaylistChannelParseModel>
}
