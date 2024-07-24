/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 27.06.24, 14:40
 *
 */

package com.mvproject.tinyiptvkmp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mvproject.tinyiptvkmp.database.entity.EpgInfoEntity

@Dao
interface EpgInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpgInfo(data: List<EpgInfoEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateEpgInfo(data: EpgInfoEntity)

    @Query("SELECT * FROM EpgInfoEntity")
    suspend fun getEpgInfo(): List<EpgInfoEntity>

    @Query("DELETE FROM EpgInfoEntity")
    suspend fun deleteEpgInfo()
}
