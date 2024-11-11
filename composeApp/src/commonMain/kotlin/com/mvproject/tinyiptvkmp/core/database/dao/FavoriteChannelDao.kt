/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.07.24, 18:38
 *
 */

package com.mvproject.tinyiptvkmp.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mvproject.tinyiptvkmp.core.database.entity.FavoriteChannelEntity

@Dao
interface FavoriteChannelDao {
    @Query("UPDATE favoriteChannels SET channelName = :channelName WHERE channelUrl = :channelUrl")
    suspend fun updateFavoriteChannels(
        channelName: String,
        channelUrl: String,
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteChannel(data: FavoriteChannelEntity)

    @Query("SELECT COUNT(*) FROM favoriteChannels WHERE parentListId == (SELECT id FROM playlists WHERE isSelected == 1)")
    suspend fun getFavoriteChannelCount(): Int

    @Query("SELECT * FROM favoriteChannels WHERE parentListId == (SELECT id FROM playlists WHERE isSelected == 1)")
    suspend fun getSelectedFavoriteChannels(): List<FavoriteChannelEntity>

    @Query("SELECT * FROM favoriteChannels WHERE parentListId == :id")
    suspend fun getFavoriteChannelById(id: String): List<FavoriteChannelEntity>

    @Query("SELECT channelUrl FROM favoriteChannels")
    suspend fun getFavoriteChannelUrls(): List<String>

    @Query(
        "DELETE FROM favoriteChannels WHERE parentListId == (SELECT id FROM playlists WHERE isSelected == 1) AND channelUrl = :channelUrl",
    )
    suspend fun deleteChannelFromFavorite(channelUrl: String)

    @Query("DELETE FROM favoriteChannels WHERE parentListId = :id")
    suspend fun deletePlaylistFavoriteChannelEntities(id: String)
}
