/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 17.05.24, 18:15
 *
 */

package com.mvproject.tinyiptvkmp.data.repository

import androidx.room.Transaction
import com.mvproject.tinyiptvkmp.data.mappers.EntityMapper.toChannelEntity
import com.mvproject.tinyiptvkmp.data.mappers.EntityMapper.toPlaylistChannel
import com.mvproject.tinyiptvkmp.data.model.channels.PlaylistChannel
import com.mvproject.tinyiptvkmp.database.AppDatabase

class PlaylistChannelsRepository(
    private val appDatabase: AppDatabase,
) {
    private val playlistChannelDao = appDatabase.playlistChannelDao()

    @Transaction
    suspend fun addPlaylistChannels(channels: List<PlaylistChannel>) {
        val channelsData =
            channels.map {
                it.toChannelEntity()
            }

        playlistChannelDao.insertPlaylistChannels(data = channelsData)
    }

    @Transaction
    suspend fun updatePlaylistChannels(channels: List<PlaylistChannel>) {
        val channelsData =
            channels.map {
                it.toChannelEntity()
            }
        playlistChannelDao.updatePlaylistChannels(data = channelsData)
    }

    suspend fun loadPlaylistGroups(listId: Long): List<String> {
        return playlistChannelDao.getPlaylistChannelsGroups(id = listId)
            .distinctBy { it }
    }

    suspend fun loadPlaylistChannelsCount(listId: Long): Int {
        return playlistChannelDao.getPlaylistChannelsCount(id = listId)
    }

    suspend fun loadPlaylistGroupChannelsCount(
        listId: Long,
        group: String,
    ): Int {
        return playlistChannelDao.getPlaylistGroupChannelsCount(id = listId, group = group)
    }

    suspend fun loadChannelsById(listId: Long): List<PlaylistChannel> {
        return playlistChannelDao.getPlaylistChannelsById(id = listId)
            .map { entity ->
                entity.toPlaylistChannel()
            }
    }

    suspend fun loadChannels(): List<PlaylistChannel> {
        return playlistChannelDao.getPlaylistChannels()
            .map { entity ->
                entity.toPlaylistChannel()
            }
    }

    suspend fun loadPlaylistChannelsByUrls(
        listId: Long,
        urls: List<String>,
    ): List<PlaylistChannel> {
        return playlistChannelDao.getChannelsByUrls(id = listId, urls = urls)
            .map { entity ->
                entity.toPlaylistChannel()
            }
    }

    suspend fun loadPlaylistGroupChannels(
        listId: Long,
        group: String,
    ): List<PlaylistChannel> {
        return playlistChannelDao.getChannelsByPlaylistGroup(id = listId, group = group)
            .map { entity ->
                entity.toPlaylistChannel()
            }
    }

    suspend fun deletePlaylistChannels(listId: Long) {
        playlistChannelDao.deletePlaylistChannels(id = listId)
    }
}
