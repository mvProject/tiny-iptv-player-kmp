/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.05.24, 20:00
 *
 */

package com.mvproject.tinyiptvkmp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mvproject.tinyiptvkmp.database.entity.EpgProgramEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EpgProgramDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrograms(data: List<EpgProgramEntity>)

    @Query("SELECT * FROM EpgProgramEntity WHERE EpgProgramEntity.channelId IN (:ids) AND programEnd > :time")
    suspend fun getPrograms(
        ids: List<String>,
        time: Long,
    ): List<EpgProgramEntity>

    @Query("SELECT * FROM EpgProgramEntity WHERE EpgProgramEntity.channelId = :id AND programEnd > :time")
    suspend fun getProgram(
        id: String,
        time: Long,
    ): List<EpgProgramEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistInfo(data: EpgProgramEntity)

    @Query("SELECT * FROM EpgProgramEntity")
    fun getAllPlaylistInfo(): Flow<List<EpgProgramEntity>>

    @Query("DELETE FROM EpgProgramEntity WHERE EpgProgramEntity.channelId IN (:ids)")
    suspend fun deletePrograms(ids: List<String>)

    @Query("DELETE FROM EpgProgramEntity WHERE EpgProgramEntity.channelId = :id")
    suspend fun deleteProgram(id: String)
}
