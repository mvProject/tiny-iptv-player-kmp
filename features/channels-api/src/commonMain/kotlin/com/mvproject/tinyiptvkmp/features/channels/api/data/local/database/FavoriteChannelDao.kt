package com.mvproject.tinyiptvkmp.features.channels.api.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteChannelDao {
    @Query("UPDATE favoriteChannels SET channelName = :channelName WHERE channelUrl = :channelUrl")
    suspend fun updateFavoriteChannels(
        channelName: String,
        channelUrl: String,
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteChannel(data: FavoriteChannelEntity)

    @Query("SELECT COUNT(*) FROM favoriteChannels WHERE parentListId = :playlistId")
    suspend fun getFavoriteChannelCount(playlistId: String): Int

    @Query("SELECT * FROM favoriteChannels WHERE parentListId = :playlistId")
    suspend fun getSelectedFavoriteChannels(playlistId: String): List<FavoriteChannelEntity>

    @Query("SELECT * FROM favoriteChannels WHERE parentListId = :playlistId")
    suspend fun getFavoriteChannelsByPlaylistId(playlistId: String): List<FavoriteChannelEntity>

    @Query("SELECT channelUrl FROM favoriteChannels")
    suspend fun getFavoriteChannelUrls(): List<String>

    @Query("DELETE FROM favoriteChannels WHERE parentListId = :playlistId AND channelUrl = :channelUrl")
    suspend fun deleteChannelFromFavorite(
        playlistId: String,
        channelUrl: String,
    )

    @Query("DELETE FROM favoriteChannels WHERE parentListId = :playlistId")
    suspend fun deletePlaylistFavoriteChannelEntities(playlistId: String)
}
