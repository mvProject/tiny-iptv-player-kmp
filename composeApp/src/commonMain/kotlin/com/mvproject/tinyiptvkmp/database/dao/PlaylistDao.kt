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
import com.mvproject.tinyiptvkmp.database.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Upsert
    suspend fun savePlaylist(data: PlaylistEntity)

    @Upsert
    suspend fun savePlaylists(data: List<PlaylistEntity>)

    @Query("SELECT * FROM playlists")
    fun getAllPlaylistsAsFlow(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists")
    suspend fun getAllPlaylists(): List<PlaylistEntity>

    @Query("SELECT id FROM playlists WHERE isSelected==1")
    suspend fun getSelectedPlaylistId(): Long

    @Query("SELECT * FROM playlists WHERE id = :id")
    suspend fun getPlaylistById(id: Long): PlaylistEntity

    @Query("DELETE FROM playlists WHERE id = :id")
    suspend fun deletePlaylist(id: Long)
}
