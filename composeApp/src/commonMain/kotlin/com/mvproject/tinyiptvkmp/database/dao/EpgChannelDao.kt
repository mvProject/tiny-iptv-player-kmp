/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 27.06.24, 14:40
 *
 */

package com.mvproject.tinyiptvkmp.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.mvproject.tinyiptvkmp.database.entity.EpgChannelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EpgChannelDao {
    @Query("SELECT * FROM epgChannels")
    suspend fun getEpgInfo(): List<EpgChannelEntity>

    @Query("SELECT * FROM epgChannels")
    fun getEpgChannels(): Flow<List<EpgChannelEntity>>

    @Query("DELETE FROM epgChannels")
    suspend fun deleteEpgChannels()

    @Upsert
    suspend fun insertEpgChannels(data: List<EpgChannelEntity>)
}
