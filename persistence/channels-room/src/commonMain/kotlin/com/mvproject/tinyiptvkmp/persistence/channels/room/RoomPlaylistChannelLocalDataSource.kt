package com.mvproject.tinyiptvkmp.persistence.channels.room

import androidx.room.Transaction
import com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.local.PlaylistChannelLocalDataSource
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannel
import com.mvproject.tinyiptvkmp.persistence.channels.room.database.PlaylistChannelDao
import com.mvproject.tinyiptvkmp.persistence.channels.room.mapper.toChannelEntity
import com.mvproject.tinyiptvkmp.persistence.channels.room.mapper.toPlaylistChannel

internal class RoomPlaylistChannelLocalDataSource(
    private val playlistChannelDao: PlaylistChannelDao,
) : PlaylistChannelLocalDataSource {
    @Transaction
    override suspend fun savePlaylistChannels(channels: List<PlaylistChannel>) {
        playlistChannelDao.savePlaylistChannels(data = channels.map { it.toChannelEntity() })
    }

    override suspend fun loadPlaylistGroups(playlistId: String): List<String> =
        playlistChannelDao.getPlaylistChannelsGroups(playlistId = playlistId)

    override suspend fun loadPlaylistGroupCounts(playlistId: String): Map<String, Int> =
        playlistChannelDao.getPlaylistGroupCounts(playlistId = playlistId)
            .associate { groupCount -> groupCount.groupName to groupCount.groupContentCount }

    override suspend fun loadPlaylistChannelsCount(playlistId: String): Int =
        playlistChannelDao.getPlaylistChannelsCount(playlistId = playlistId)

    override suspend fun loadPlaylistGroupChannelsCount(
        playlistId: String,
        group: String,
    ): Int =
        playlistChannelDao.getPlaylistGroupChannelsCount(
            playlistId = playlistId,
            group = group,
        )

    override suspend fun loadChannelsById(playlistId: String): List<PlaylistChannel> =
        playlistChannelDao.getPlaylistChannelsById(playlistId = playlistId)
            .map { it.toPlaylistChannel() }

    override suspend fun loadAllChannels(): List<PlaylistChannel> =
        playlistChannelDao.getAllChannels().map { it.toPlaylistChannel() }

    override suspend fun loadPlaylistChannelsByUrls(
        playlistId: String,
        urls: List<String>,
    ): List<PlaylistChannel> =
        playlistChannelDao.getChannelsByUrls(
            playlistId = playlistId,
            urls = urls,
        ).map { it.toPlaylistChannel() }

    override suspend fun loadPlaylistGroupChannels(
        playlistId: String,
        group: String,
    ): List<PlaylistChannel> =
        playlistChannelDao.getChannelsByPlaylistGroup(
            playlistId = playlistId,
            group = group,
        ).map { it.toPlaylistChannel() }

    override suspend fun deletePlaylistChannels(id: String) {
        playlistChannelDao.deletePlaylistChannels(id = id)
    }
}