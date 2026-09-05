package com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.local

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannelWindow
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.TvChannel

interface PlaylistChannelLocalDataSource {
    suspend fun savePlaylistChannels(channels: List<PlaylistChannel>)

    suspend fun loadPlaylistGroups(playlistId: String): List<String>

    suspend fun loadPlaylistGroupCounts(playlistId: String): Map<String, Int>

    suspend fun loadPlaylistChannelsCount(playlistId: String): Int

    suspend fun loadPlaylistGroupChannelsCount(
        playlistId: String,
        group: String,
    ): Int

    suspend fun loadChannelsById(playlistId: String): List<PlaylistChannel>

    suspend fun loadAllChannels(): List<PlaylistChannel>

    suspend fun loadPlaylistChannelsByUrls(
        playlistId: String,
        urls: List<String>,
    ): List<PlaylistChannel>

    suspend fun loadPlaylistGroupChannels(
        playlistId: String,
        group: String,
    ): List<PlaylistChannel>

    suspend fun loadPlaylistChannelsWithFavorites(
        playlistId: String,
        offset: Int,
        limit: Int,
        searchQuery: String,
    ): List<TvChannel>

    suspend fun loadPlaylistGroupChannelsWithFavorites(
        playlistId: String,
        group: String,
        offset: Int,
        limit: Int,
        searchQuery: String,
    ): List<TvChannel>

    suspend fun loadFavoritePlaylistChannels(
        playlistId: String,
        favoriteType: FavoriteType,
        offset: Int,
        limit: Int,
        searchQuery: String,
    ): List<TvChannel>

    suspend fun loadPlaylistChannelWindowWithFavorites(
        playlistId: String,
        channelUrl: String,
        before: Int,
        after: Int,
    ): PlaylistChannelWindow?

    suspend fun loadPlaylistGroupChannelWindowWithFavorites(
        playlistId: String,
        group: String,
        channelUrl: String,
        before: Int,
        after: Int,
    ): PlaylistChannelWindow?

    suspend fun loadFavoritePlaylistChannelWindow(
        playlistId: String,
        favoriteType: FavoriteType,
        channelUrl: String,
        before: Int,
        after: Int,
    ): PlaylistChannelWindow?

    suspend fun deletePlaylistChannels(id: String)
}
