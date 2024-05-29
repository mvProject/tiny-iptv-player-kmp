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
import com.mvproject.tinyiptvkmp.database.entity.FavoriteChannelEntity

@Dao
interface FavoriteChannelDao {
    @Query("UPDATE FavoriteChannelEntity SET channelName = :channelName WHERE channelUrl = :channelUrl")
    suspend fun updateFavoriteChannels(
        channelName: String,
        channelUrl: String,
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteChannel(data: FavoriteChannelEntity)

    @Query("SELECT COUNT(*) FROM FavoriteChannelEntity WHERE parentListId = :id")
    suspend fun getFavoriteChannelCount(id: Long): Int

    @Query("SELECT channelUrl FROM FavoriteChannelEntity WHERE parentListId = :id")
    suspend fun getPlaylistFavoriteChannelUrls(id: Long): List<String>

    @Query("SELECT channelUrl FROM FavoriteChannelEntity")
    suspend fun getFavoriteChannelUrls(): List<String>

    @Query("DELETE FROM FavoriteChannelEntity WHERE parentListId = :id AND channelUrl = :channelUrl")
    suspend fun deleteChannelFromFavorite(
        id: Long,
        channelUrl: String,
    )

    @Query("DELETE FROM FavoriteChannelEntity WHERE parentListId = :id")
    suspend fun deletePlaylistFavoriteChannelEntities(id: Long)
}
