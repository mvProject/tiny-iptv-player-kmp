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

    @Query("SELECT * FROM PlaylistEntity")
    fun getAllPlaylistsAsFlow(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM PlaylistEntity")
    suspend fun getAllPlaylists(): List<PlaylistEntity>

    @Query("SELECT COUNT(*) FROM PlaylistEntity ")
    suspend fun getAllPlaylistsCount(): Int

    @Query("SELECT * FROM PlaylistEntity WHERE id = :id")
    suspend fun getPlaylistById(id: Long): PlaylistEntity

    @Query("DELETE FROM PlaylistEntity WHERE id = :id")
    suspend fun deletePlaylist(id: Long)
}
