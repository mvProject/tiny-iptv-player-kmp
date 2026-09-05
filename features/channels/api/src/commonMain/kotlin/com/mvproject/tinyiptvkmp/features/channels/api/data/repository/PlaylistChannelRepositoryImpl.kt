package com.mvproject.tinyiptvkmp.features.channels.api.data.repository

import com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.content.PlaylistContentLocalDataSource
import com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.local.PlaylistChannelLocalDataSource
import com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.remote.PlaylistChannelRemoteDataSource
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannelWindow
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.PlaylistChannelRepository

internal class PlaylistChannelRepositoryImpl(
    private val localDataSource: PlaylistChannelLocalDataSource,
    private val localContentDataSource: PlaylistContentLocalDataSource,
    private val remoteDataSource: PlaylistChannelRemoteDataSource,
) : PlaylistChannelRepository {
    override suspend fun savePlaylistChannels(channels: List<PlaylistChannel>) {
        localDataSource.savePlaylistChannels(channels)
    }

    override suspend fun loadLocalPlaylistChannels(
        playlistId: String,
        source: String,
    ): List<PlaylistChannel> =
        localContentDataSource.loadPlaylistContent(
            playlistId = playlistId,
            source = source,
        )

    override suspend fun loadRemotePlaylistChannels(
        playlistId: String,
        source: String,
    ): List<PlaylistChannel> =
        remoteDataSource.loadPlaylistContent(
            playlistId = playlistId,
            url = source,
        )

    override suspend fun loadPlaylistGroups(playlistId: String): List<String> =
        localDataSource.loadPlaylistGroups(playlistId = playlistId)

    override suspend fun loadPlaylistGroupCounts(playlistId: String): Map<String, Int> =
        localDataSource.loadPlaylistGroupCounts(playlistId = playlistId)

    override suspend fun loadPlaylistChannelsCount(playlistId: String): Int =
        localDataSource.loadPlaylistChannelsCount(playlistId = playlistId)

    override suspend fun loadPlaylistGroupChannelsCount(playlistId: String, group: String): Int =
        localDataSource.loadPlaylistGroupChannelsCount(
            playlistId = playlistId,
            group = group,
        )

    override suspend fun loadChannelsById(playlistId: String): List<PlaylistChannel> =
        localDataSource.loadChannelsById(playlistId = playlistId)

    override suspend fun loadAllChannels(): List<PlaylistChannel> =
        localDataSource.loadAllChannels()

    override suspend fun loadPlaylistChannelsByUrls(
        playlistId: String,
        urls: List<String>,
    ): List<PlaylistChannel> =
        localDataSource.loadPlaylistChannelsByUrls(
            playlistId = playlistId,
            urls = urls,
        )

    override suspend fun loadPlaylistGroupChannels(
        playlistId: String,
        group: String,
    ): List<PlaylistChannel> =
        localDataSource.loadPlaylistGroupChannels(
            playlistId = playlistId,
            group = group,
        )

    override suspend fun loadPlaylistChannelsWithFavorites(
        playlistId: String,
        offset: Int,
        limit: Int,
        searchQuery: String,
    ): List<TvChannel> =
        localDataSource.loadPlaylistChannelsWithFavorites(
            playlistId = playlistId,
            offset = offset,
            limit = limit,
            searchQuery = searchQuery,
        )

    override suspend fun loadPlaylistGroupChannelsWithFavorites(
        playlistId: String,
        group: String,
        offset: Int,
        limit: Int,
        searchQuery: String,
    ): List<TvChannel> =
        localDataSource.loadPlaylistGroupChannelsWithFavorites(
            playlistId = playlistId,
            group = group,
            offset = offset,
            limit = limit,
            searchQuery = searchQuery,
        )

    override suspend fun loadFavoritePlaylistChannels(
        playlistId: String,
        favoriteType: FavoriteType,
        offset: Int,
        limit: Int,
        searchQuery: String,
    ): List<TvChannel> =
        localDataSource.loadFavoritePlaylistChannels(
            playlistId = playlistId,
            favoriteType = favoriteType,
            offset = offset,
            limit = limit,
            searchQuery = searchQuery,
        )

    override suspend fun loadPlaylistChannelWindowWithFavorites(
        playlistId: String,
        channelUrl: String,
        before: Int,
        after: Int,
    ): PlaylistChannelWindow? =
        localDataSource.loadPlaylistChannelWindowWithFavorites(
            playlistId = playlistId,
            channelUrl = channelUrl,
            before = before,
            after = after,
        )

    override suspend fun loadPlaylistGroupChannelWindowWithFavorites(
        playlistId: String,
        group: String,
        channelUrl: String,
        before: Int,
        after: Int,
    ): PlaylistChannelWindow? =
        localDataSource.loadPlaylistGroupChannelWindowWithFavorites(
            playlistId = playlistId,
            group = group,
            channelUrl = channelUrl,
            before = before,
            after = after,
        )

    override suspend fun loadFavoritePlaylistChannelWindow(
        playlistId: String,
        favoriteType: FavoriteType,
        channelUrl: String,
        before: Int,
        after: Int,
    ): PlaylistChannelWindow? =
        localDataSource.loadFavoritePlaylistChannelWindow(
            playlistId = playlistId,
            favoriteType = favoriteType,
            channelUrl = channelUrl,
            before = before,
            after = after,
        )

    override suspend fun deletePlaylistChannels(listId: String) {
        localDataSource.deletePlaylistChannels(id = listId)
    }
}
