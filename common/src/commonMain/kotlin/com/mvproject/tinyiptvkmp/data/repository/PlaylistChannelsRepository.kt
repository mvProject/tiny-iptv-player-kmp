/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:29
 *
 */

package com.mvproject.tinyiptvkmp.data.repository

import androidx.room.Transaction
import com.mvproject.tinyiptvkmp.data.mappers.EntityMapper.toChannelEntity
import com.mvproject.tinyiptvkmp.data.mappers.EntityMapper.toPlaylistChannel
import com.mvproject.tinyiptvkmp.data.model.channels.PlaylistChannel
import com.mvproject.tinyiptvkmp.database.AppDatabase

class PlaylistChannelsRepository(
    //  private val db: TinyIptvKmpDatabase,
    private val appDatabase: AppDatabase,
) {
    //   private val playlistChannelQueries = db.playlistChannelEntityQueries
    private val playlistChannelDao = appDatabase.playlistChannelDao()

    /*    suspend fun addPlaylistChannels(channels: List<PlaylistChannel>) {
            withContext(Dispatchers.IO) {
                playlistChannelQueries.transaction {
                    channels.forEach { channel ->
                        playlistChannelQueries.addChannelEntity(
                            channel.toPlaylistChannelEntity(),
                        )
                    }
                }
            }
        }*/

    @Transaction
    suspend fun addPlaylistChannels(channels: List<PlaylistChannel>) {
        val channelsData =
            channels.map {
                it.toChannelEntity()
            }

        playlistChannelDao.insertPlaylistChannels(data = channelsData)
    }

    /*    suspend fun updatePlaylistChannels(channels: List<PlaylistChannel>) {
            withContext(Dispatchers.IO) {
                playlistChannelQueries.transaction {
                    channels.forEach { channel ->
                        playlistChannelQueries.updateChannelEntity(
                            channelGroup = channel.channelGroup,
                            channelName = channel.channelName,
                            channelLogo = channel.channelLogo,
                            channelUrl = channel.channelUrl,
                            epgId = channel.epgId,
                        )
                    }
                }
            }
        }*/
    @Transaction
    suspend fun updatePlaylistChannels(channels: List<PlaylistChannel>) {
        val channelsData =
            channels.map {
                it.toChannelEntity()
            }
        playlistChannelDao.updatePlaylistChannels(data = channelsData)
        // withContext(Dispatchers.IO) {
        //    playlistChannelQueries.transaction {
        //        channels.forEach { channel ->
        //            playlistChannelQueries.updateChannelEntity(
        //                channelGroup = channel.channelGroup,
        //                channelName = channel.channelName,
        //                channelLogo = channel.channelLogo,
        //                channelUrl = channel.channelUrl,
        //                epgId = channel.epgId,
        //            )
        //        }
        //    }
        // }
    }

    /*    fun loadPlaylistGroups(listId: Long): List<String> {
            return playlistChannelQueries.getPlaylistChannelGroups(id = listId)
                .executeAsList()
                .distinctBy { it }
        }*/

    suspend fun loadPlaylistGroups(listId: Long): List<String> {
        return playlistChannelDao.getPlaylistChannelsGroups(id = listId)
            .distinctBy { it }
    }

    /*    fun loadPlaylistChannelsCount(listId: Long): Int {
            return playlistChannelQueries.getPlaylistChannelsCount(id = listId)
                .executeAsOne()
                .toInt()
        }*/

    suspend fun loadPlaylistChannelsCount(listId: Long): Int {
        return playlistChannelDao.getPlaylistChannelsCount(id = listId)
    }

    /*    fun loadPlaylistGroupChannelsCount(
            listId: Long,
            group: String,
        ): Int {
            return playlistChannelQueries.getPlaylistGroupChannelsCount(id = listId, group = group)
                .executeAsOne()
                .toInt()
        }*/

    suspend fun loadPlaylistGroupChannelsCount(
        listId: Long,
        group: String,
    ): Int {
        return playlistChannelDao.getPlaylistGroupChannelsCount(id = listId, group = group)
    }

    /*    fun loadPlaylistChannels(listId: Long): List<PlaylistChannel> {
            return playlistChannelQueries.getPlaylistChannelsEntities(id = listId)
                .executeAsList()
                .map { entity ->
                    entity.toPlaylistChannel()
                }
        }*/

    suspend fun loadChannelsById(listId: Long): List<PlaylistChannel> {
        return playlistChannelDao.getPlaylistChannelsById(id = listId)
            .map { entity ->
                entity.toPlaylistChannel()
            }
    }

    /*    fun loadChannels(): List<PlaylistChannel> {
            return playlistChannelQueries.getChannelsEntities()
                .executeAsList()
                .map { entity ->
                    entity.toPlaylistChannel()
                }
        }*/

    suspend fun loadChannels(): List<PlaylistChannel> {
        return playlistChannelDao.getPlaylistChannels()
            .map { entity ->
                entity.toPlaylistChannel()
            }
    }

    /*    fun loadPlaylistChannelsByUrls(
            listId: Long,
            urls: List<String>,
        ): List<PlaylistChannel> {
            return playlistChannelQueries.getPlaylistChannelsEntitiesByUrls(id = listId, urls = urls)
                .executeAsList()
                .map { entity ->
                    entity.toPlaylistChannel()
                }
        }*/

    suspend fun loadPlaylistChannelsByUrls(
        listId: Long,
        urls: List<String>,
    ): List<PlaylistChannel> {
        return playlistChannelDao.getChannelsByUrls(id = listId, urls = urls)
            .map { entity ->
                entity.toPlaylistChannel()
            }
    }

    /*    fun loadPlaylistGroupChannels(
            listId: Long,
            group: String,
        ): List<PlaylistChannel> {
            return playlistChannelQueries.getPlaylistGroupChannelEntities(id = listId, group = group)
                .executeAsList()
                .map { entity ->
                    entity.toPlaylistChannel()
                }
        }*/

    suspend fun loadPlaylistGroupChannels(
        listId: Long,
        group: String,
    ): List<PlaylistChannel> {
        return playlistChannelDao.getChannelsByPlaylistGroup(id = listId, group = group)
            .map { entity ->
                entity.toPlaylistChannel()
            }
    }

    /*    suspend fun deletePlaylistChannels(listId: Long) {
            withContext(Dispatchers.IO) {
                playlistChannelQueries.transaction {
                    playlistChannelQueries.deletePlaylistChannelsEntities(id = listId)
                }
            }
        }*/

    suspend fun deletePlaylistChannels(listId: Long) {
        playlistChannelDao.deletePlaylistChannels(id = listId)
    }
}
