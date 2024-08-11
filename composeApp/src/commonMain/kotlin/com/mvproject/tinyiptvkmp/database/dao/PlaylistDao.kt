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
import com.mvproject.tinyiptvkmp.database.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistInfo(data: PlaylistEntity)

    @Query("SELECT * FROM PlaylistEntity")
    fun getAllPlaylistInfoFlow(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM PlaylistEntity")
    suspend fun getAllPlaylistInfo(): List<PlaylistEntity>

    @Query("SELECT COUNT(*) FROM PlaylistEntity ")
    suspend fun getAllPlaylistCount(): Int

    @Query("SELECT * FROM PlaylistEntity WHERE id = :id")
    suspend fun getPlaylistInfoById(id: Long): PlaylistEntity

    @Query("DELETE FROM PlaylistEntity WHERE id = :id")
    suspend fun deleteSinglePlaylistInfo(id: Long)
}
