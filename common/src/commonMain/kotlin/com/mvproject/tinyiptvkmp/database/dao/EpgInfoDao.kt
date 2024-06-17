/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 10.06.24, 11:39
 *
 */

package com.mvproject.tinyiptvkmp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mvproject.tinyiptvkmp.database.entity.EpgInfoEntity

@Dao
interface EpgInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpgInfo(data: List<EpgInfoEntity>)

    @Query("SELECT * FROM EpgInfoEntity")
    suspend fun getEpgInfo(): List<EpgInfoEntity>

    @Query("DELETE FROM EpgInfoEntity")
    suspend fun deleteEpgInfo()
}
