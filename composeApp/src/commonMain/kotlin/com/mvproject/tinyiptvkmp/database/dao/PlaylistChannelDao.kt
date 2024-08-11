/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:06
 *
 */

package com.mvproject.tinyiptvkmp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mvproject.tinyiptvkmp.database.entity.PlaylistChannelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistChannelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistChannels(data: List<PlaylistChannelEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlaylistChannels(data: List<PlaylistChannelEntity>)

    @Query("SELECT * FROM PlaylistChannelEntity")
    suspend fun getPlayerListAsFlow(): List<PlaylistChannelEntity>

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistInfo(data: PlaylistChannelEntity)

    @Query("SELECT * FROM PlaylistChannelEntity")
    suspend fun getPlaylistChannels(): List<PlaylistChannelEntity>

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

    @Query("SELECT * FROM PlaylistChannelEntity")
    fun getAllPlaylistInfo(): Flow<List<PlaylistChannelEntity>>

    @Query("DELETE FROM PlaylistChannelEntity WHERE parentListId = :id")
    suspend fun deletePlaylistChannels(id: Long)
}
