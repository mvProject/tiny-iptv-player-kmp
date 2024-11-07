/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 17.05.24, 18:15
 *
 */

package com.mvproject.tinyiptvkmp.core.data.repository

import androidx.room.Transaction
import com.mvproject.tinyiptvkmp.core.data.model.PlaylistChannel
import com.mvproject.tinyiptvkmp.core.database.db.AppDatabase
import com.mvproject.tinyiptvkmp.core.domain.mappers.EntityMapper.toChannelEntity
import com.mvproject.tinyiptvkmp.core.domain.mappers.EntityMapper.toPlaylistChannel

class PlaylistChannelsRepository(
    private val appDatabase: AppDatabase,
) {
    private val playlistChannelDao = appDatabase.playlistChannelDao()

    @Transaction
    suspend fun savePlaylistChannels(channels: List<PlaylistChannel>) {
        val channelsData =
            channels.map {
                it.toChannelEntity()
            }
        playlistChannelDao.savePlaylistChannels(data = channelsData)
    }

    suspend fun loadPlaylistGroups(): List<String> =
        playlistChannelDao
            .getPlaylistChannelsGroups()
            .distinctBy { it }

    suspend fun loadPlaylistChannelsCount(): Int = playlistChannelDao.getPlaylistChannelsCount()

    suspend fun loadPlaylistGroupChannelsCount(group: String): Int =
        playlistChannelDao
            .getPlaylistGroupChannelsCount(group = group)

    suspend fun loadChannelsById(): List<PlaylistChannel> =
        playlistChannelDao
            .getPlaylistChannelsById()
            .map { entity ->
                entity.toPlaylistChannel()
            }

    suspend fun loadAllChannels(): List<PlaylistChannel> =
        playlistChannelDao
            .getAllChannels()
            .map { entity ->
                entity.toPlaylistChannel()
            }

    suspend fun loadPlaylistChannelsByUrls(urls: List<String>): List<PlaylistChannel> =
        playlistChannelDao
            .getChannelsByUrls(urls = urls)
            .map { entity ->
                entity.toPlaylistChannel()
            }

    suspend fun loadPlaylistGroupChannels(group: String): List<PlaylistChannel> =
        playlistChannelDao
            .getChannelsByPlaylistGroup(group = group)
            .map { entity ->
                entity.toPlaylistChannel()
            }

    suspend fun deletePlaylistChannels(listId: Long) {
        playlistChannelDao.deletePlaylistChannels(id = listId)
    }
}
