/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.05.24, 20:00
 *
 */

package com.mvproject.tinyiptvkmp.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mvproject.tinyiptvkmp.core.database.entity.EpgProgramEntity

@Dao
interface EpgProgramDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrograms(data: List<EpgProgramEntity>)

    @Query("SELECT * FROM epgPrograms WHERE channelId IN (:ids) AND dateTimeEnd > :time")
    suspend fun getPrograms(
        ids: List<String>,
        time: Long,
    ): List<EpgProgramEntity>

    @Query("SELECT * FROM epgPrograms WHERE channelId = :id AND dateTimeEnd > :time")
    suspend fun getProgram(
        id: String,
        time: Long,
    ): List<EpgProgramEntity>

    @Query("DELETE FROM epgPrograms WHERE channelId = :id")
    suspend fun deleteProgram(id: String)

    @Query("DELETE FROM epgPrograms WHERE dateTimeEnd < :timeStamp")
    suspend fun deleteProgramsByDate(timeStamp: Long) :Int
}
