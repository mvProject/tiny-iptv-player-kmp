package com.mvproject.tinyiptvkmp.features.channels.api.data.repository

import com.mvproject.tinyiptvkmp.features.channels.api.data.local.PlaylistChannelLocalDataSource
import com.mvproject.tinyiptvkmp.features.channels.api.data.mapper.toChannelEntity
import com.mvproject.tinyiptvkmp.features.channels.api.data.mapper.toPlaylistChannel
import com.mvproject.tinyiptvkmp.features.channels.api.data.remote.PlaylistChannelRemoteDataSource
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannelSource
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannelSourceType
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.PlaylistChannelRepository
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.SelectedPlaylistProvider

internal class PlaylistChannelRepositoryImpl(
    private val local: PlaylistChannelLocalDataSource,
    private val remote: PlaylistChannelRemoteDataSource,
    private val selectedPlaylistProvider: SelectedPlaylistProvider,
) : PlaylistChannelRepository {
    override suspend fun savePlaylistChannels(channels: List<PlaylistChannel>) {
        local.savePlaylistChannels(channels.map { it.toChannelEntity() })
    }

    override suspend fun loadPlaylistChannels(source: PlaylistChannelSource): List<PlaylistChannel> =
        when (source.sourceType) {
            PlaylistChannelSourceType.LOCAL -> local.loadPlaylistContent(source = source.source)
            PlaylistChannelSourceType.REMOTE -> remote.loadPlaylistContent(url = source.source)
        }.map { it.toPlaylistChannel(id = source.parentListId) }

    override suspend fun loadPlaylistGroups(): List<String> =
        local.loadPlaylistGroups(
            playlistId = selectedPlaylistProvider.getSelectedPlaylistId(),
        ).distinct()

    override suspend fun loadPlaylistChannelsCount(): Int =
        local.loadPlaylistChannelsCount(
            playlistId = selectedPlaylistProvider.getSelectedPlaylistId(),
        )

    override suspend fun loadPlaylistGroupChannelsCount(group: String): Int =
        local.loadPlaylistGroupChannelsCount(
            playlistId = selectedPlaylistProvider.getSelectedPlaylistId(),
            group = group,
        )

    override suspend fun loadChannelsById(): List<PlaylistChannel> =
        local.loadChannelsById(
            playlistId = selectedPlaylistProvider.getSelectedPlaylistId(),
        ).map { it.toPlaylistChannel() }

    override suspend fun loadAllChannels(): List<PlaylistChannel> =
        local.loadAllChannels().map { it.toPlaylistChannel() }

    override suspend fun loadPlaylistChannelsByUrls(urls: List<String>): List<PlaylistChannel> =
        local.loadPlaylistChannelsByUrls(
            playlistId = selectedPlaylistProvider.getSelectedPlaylistId(),
            urls = urls,
        ).map { it.toPlaylistChannel() }

    override suspend fun loadPlaylistGroupChannels(group: String): List<PlaylistChannel> =
        local.loadPlaylistGroupChannels(
            playlistId = selectedPlaylistProvider.getSelectedPlaylistId(),
            group = group,
        ).map { it.toPlaylistChannel() }

    override suspend fun deletePlaylistChannels(listId: String) {
        local.deletePlaylistChannels(id = listId)
    }
}
