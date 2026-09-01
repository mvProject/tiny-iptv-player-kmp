package com.mvproject.tinyiptvkmp.features.channels.api.data.repository

import com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.content.PlaylistContentLocalDataSource
import com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.local.PlaylistChannelLocalDataSource
import com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.remote.PlaylistChannelRemoteDataSource
import com.mvproject.tinyiptvkmp.features.channels.api.data.mappers.toPlaylistChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannel
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
        localContentDataSource.loadPlaylistContent(source = source)
            .map { it.toPlaylistChannel(id = playlistId) }

    override suspend fun loadRemotePlaylistChannels(
        playlistId: String,
        source: String,
    ): List<PlaylistChannel> =
        remoteDataSource.loadPlaylistContent(url = source)
            .map { it.toPlaylistChannel(id = playlistId) }

    override suspend fun loadPlaylistGroups(playlistId: String): List<String> =
        localDataSource.loadPlaylistGroups(playlistId = playlistId).distinct()

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

    override suspend fun deletePlaylistChannels(listId: String) {
        localDataSource.deletePlaylistChannels(id = listId)
    }
}
