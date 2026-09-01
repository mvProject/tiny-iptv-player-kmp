package com.mvproject.tinyiptvkmp.features.channels.api.data.repository

import com.mvproject.tinyiptvkmp.features.channels.api.data.local.PlaylistChannelLocalDataSource
import com.mvproject.tinyiptvkmp.features.channels.api.data.mapper.toChannelEntity
import com.mvproject.tinyiptvkmp.features.channels.api.data.mapper.toPlaylistChannel
import com.mvproject.tinyiptvkmp.features.channels.api.data.remote.PlaylistChannelRemoteDataSource
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.PlaylistChannelRepository

internal class PlaylistChannelRepositoryImpl(
    private val local: PlaylistChannelLocalDataSource,
    private val remote: PlaylistChannelRemoteDataSource,
) : PlaylistChannelRepository {
    override suspend fun savePlaylistChannels(channels: List<PlaylistChannel>) {
        local.savePlaylistChannels(channels.map { it.toChannelEntity() })
    }

    override suspend fun loadLocalPlaylistChannels(
        playlistId: String,
        source: String,
    ): List<PlaylistChannel> =
        local.loadPlaylistContent(source = source)
            .map { it.toPlaylistChannel(id = playlistId) }

    override suspend fun loadRemotePlaylistChannels(
        playlistId: String,
        source: String,
    ): List<PlaylistChannel> =
        remote.loadPlaylistContent(url = source)
            .map { it.toPlaylistChannel(id = playlistId) }

    override suspend fun loadPlaylistGroups(playlistId: String): List<String> =
        local.loadPlaylistGroups(
            playlistId = playlistId,
        ).distinct()

    override suspend fun loadPlaylistGroupCounts(playlistId: String): Map<String, Int> =
        local.loadPlaylistGroupCounts(
            playlistId = playlistId,
        )

    override suspend fun loadPlaylistChannelsCount(playlistId: String): Int =
        local.loadPlaylistChannelsCount(
            playlistId = playlistId,
        )

    override suspend fun loadPlaylistGroupChannelsCount(playlistId: String, group: String): Int =
        local.loadPlaylistGroupChannelsCount(
            playlistId = playlistId,
            group = group,
        )

    override suspend fun loadChannelsById(playlistId: String): List<PlaylistChannel> =
        local.loadChannelsById(
            playlistId = playlistId,
        ).map { it.toPlaylistChannel() }

    override suspend fun loadAllChannels(): List<PlaylistChannel> =
        local.loadAllChannels().map { it.toPlaylistChannel() }

    override suspend fun loadPlaylistChannelsByUrls(
        playlistId: String,
        urls: List<String>,
    ): List<PlaylistChannel> =
        local.loadPlaylistChannelsByUrls(
            playlistId = playlistId,
            urls = urls,
        ).map { it.toPlaylistChannel() }

    override suspend fun loadPlaylistGroupChannels(
        playlistId: String,
        group: String,
    ): List<PlaylistChannel> =
        local.loadPlaylistGroupChannels(
            playlistId = playlistId,
            group = group,
        ).map { it.toPlaylistChannel() }

    override suspend fun deletePlaylistChannels(listId: String) {
        local.deletePlaylistChannels(id = listId)
    }
}
