/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:06
 *
 */

package com.mvproject.tinyiptvkmp.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mvproject.tinyiptvkmp.database.entity.PlaylistChannelEntity

@Dao
interface PlaylistChannelDao {
    @Upsert
    suspend fun savePlaylistChannels(data: List<PlaylistChannelEntity>)

    @Query("SELECT * FROM PlaylistChannelEntity WHERE parentListId = :id AND channelUrl IN (:urls)")
    suspend fun getChannelsByUrls(
        id: Long,
        urls: List<String>,
    ): List<PlaylistChannelEntity>

    @Query("SELECT * FROM PlaylistChannelEntity WHERE parentListId = :id AND channelGroup = :group")
    suspend fun getChannelsByPlaylistGroup(
        id: Long,
        group: String,
    ): List<PlaylistChannelEntity>

    @Query("SELECT COUNT(*) FROM PlaylistChannelEntity WHERE parentListId = :id")
    suspend fun getPlaylistChannelsCount(id: Long): Int

    @Query("SELECT COUNT(*) FROM PlaylistChannelEntity WHERE parentListId = :id AND channelGroup = :group")
    suspend fun getPlaylistGroupChannelsCount(
        id: Long,
        group: String,
    ): Int

    @Query("SELECT * FROM PlaylistChannelEntity WHERE parentListId = :id")
    suspend fun getPlaylistChannelsById(id: Long): List<PlaylistChannelEntity>

    @Query("SELECT channelGroup FROM PlaylistChannelEntity WHERE parentListId = :id")
    suspend fun getPlaylistChannelsGroups(id: Long): List<String>

    @Query("DELETE FROM PlaylistChannelEntity WHERE parentListId = :id")
    suspend fun deletePlaylistChannels(id: Long)
}
