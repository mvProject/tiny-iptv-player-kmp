/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.data.repository

import com.mvproject.tinyiptv.TinyIptvDatabase
import com.mvproject.tinyiptv.data.mappers.EntityMapper.toPlaylistChannel
import com.mvproject.tinyiptv.data.mappers.EntityMapper.toPlaylistChannelEntity
import com.mvproject.tinyiptv.data.models.channels.PlaylistChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaylistChannelsRepository(private val db: TinyIptvDatabase) {
    private val playlistChannelQueries = db.playlistChannelEntityQueries

    suspend fun addPlaylistChannels(channels: List<PlaylistChannel>) {
        withContext(Dispatchers.IO) {
            playlistChannelQueries.transaction {
                channels.forEach { channel ->
                    playlistChannelQueries.addChannelEntity(
                        channel.toPlaylistChannelEntity()
                    )
                }
            }
        }
    }

    suspend fun updatePlaylistChannels(channels: List<PlaylistChannel>) {
        withContext(Dispatchers.IO) {
            playlistChannelQueries.transaction {
                channels.forEach { channel ->
                    playlistChannelQueries.updateChannelEntity(
                        channelGroup = channel.channelGroup,
                        channelName = channel.channelName,
                        channelLogo = channel.channelLogo,
                        channelUrl = channel.channelUrl,
                        epgId = channel.epgId
                    )
                }
            }
        }
    }

    fun loadPlaylistGroups(listId: Long): List<String> {
        return playlistChannelQueries.getPlaylistChannelGroups(id = listId)
            .executeAsList()
            .distinctBy { it }
    }

    fun loadPlaylistChannelsCount(listId: Long): Int {
        return playlistChannelQueries.getPlaylistChannelsCount(id = listId)
            .executeAsOne()
            .toInt()
    }

    fun loadPlaylistGroupChannelsCount(listId: Long, group: String): Int {
        return playlistChannelQueries.getPlaylistGroupChannelsCount(id = listId, group = group)
            .executeAsOne()
            .toInt()
    }

    fun loadPlaylistChannels(listId: Long): List<PlaylistChannel> {
        return playlistChannelQueries.getPlaylistChannelsEntities(id = listId)
            .executeAsList()
            .map { entity ->
                entity.toPlaylistChannel()
            }
    }

    fun loadChannels(): List<PlaylistChannel> {
        return playlistChannelQueries.getChannelsEntities()
            .executeAsList()
            .map { entity ->
                entity.toPlaylistChannel()
            }
    }

    fun loadPlaylistChannelsByUrls(listId: Long, urls: List<String>): List<PlaylistChannel> {
        return playlistChannelQueries.getPlaylistChannelsEntitiesByUrls(id = listId, urls = urls)
            .executeAsList()
            .map { entity ->
                entity.toPlaylistChannel()
            }
    }

    fun loadPlaylistGroupChannels(listId: Long, group: String): List<PlaylistChannel> {
        return playlistChannelQueries.getPlaylistGroupChannelEntities(id = listId, group = group)
            .executeAsList()
            .map { entity ->
                entity.toPlaylistChannel()
            }
    }

    suspend fun deletePlaylistChannels(listId: Long) {
        withContext(Dispatchers.IO) {
            playlistChannelQueries.transaction {
                playlistChannelQueries.deletePlaylistChannelsEntities(id = listId)
            }
        }
    }
}