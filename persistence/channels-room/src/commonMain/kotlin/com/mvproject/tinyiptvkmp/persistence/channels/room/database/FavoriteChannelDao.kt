package com.mvproject.tinyiptvkmp.persistence.channels.room.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface FavoriteChannelDao {
    @Query(
        "UPDATE favoriteChannels SET channelName = :channelName " +
                "WHERE parentListId = :playlistId AND channelUrl = :channelUrl",
    )
    suspend fun updateFavoriteChannel(
        playlistId: String,
        channelName: String,
        channelUrl: String,
    )

    @Transaction
    suspend fun updateFavoriteChannels(
        playlistId: String,
        channelNamesByUrl: Map<String, String>,
    ) {
        channelNamesByUrl.forEach { (channelUrl, channelName) ->
            updateFavoriteChannel(
                playlistId = playlistId,
                channelName = channelName,
                channelUrl = channelUrl,
            )
        }
    }

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

    @Query("SELECT channelUrl FROM favoriteChannels WHERE parentListId = :playlistId")
    suspend fun getFavoriteChannelUrls(playlistId: String): List<String>

    @Query("DELETE FROM favoriteChannels WHERE parentListId = :playlistId AND channelUrl = :channelUrl")
    suspend fun deleteChannelFromFavorite(
        playlistId: String,
        channelUrl: String,
    )

    @Query("DELETE FROM favoriteChannels WHERE parentListId = :playlistId")
    suspend fun deletePlaylistFavoriteChannelEntities(playlistId: String)
}
