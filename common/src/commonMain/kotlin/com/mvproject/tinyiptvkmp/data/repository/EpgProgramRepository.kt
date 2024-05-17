/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 17.05.24, 18:15
 *
 */

package com.mvproject.tinyiptvkmp.data.repository

import androidx.room.Transaction
import com.mvproject.tinyiptvkmp.data.mappers.EntityMapper.toEpgProgram
import com.mvproject.tinyiptvkmp.data.mappers.EntityMapper.toEpgProgramEntity
import com.mvproject.tinyiptvkmp.data.model.epg.EpgProgram
import com.mvproject.tinyiptvkmp.database.AppDatabase

class EpgProgramRepository(
    private val appDatabase: AppDatabase,
) {
    private val epgProgramDao = appDatabase.epgProgramDao()

    suspend fun getEpgProgramsByIds(
        channelIds: List<String>,
        time: Long,
    ): List<EpgProgram> {
        return epgProgramDao.getPrograms(ids = channelIds, time = time)
            .map {
                it.toEpgProgram()
            }
    }

    suspend fun getEpgProgramsById(
        channelId: String,
        time: Long,
    ): List<EpgProgram> {
        return epgProgramDao.getProgram(id = channelId, time = time)
            .map {
                it.toEpgProgram()
            }
    }

    @Transaction
    suspend fun insertEpgPrograms(
        channelId: String,
        channelEpgPrograms: List<EpgProgram>,
    ) {
        epgProgramDao.deleteProgram(id = channelId)
        val programs =
            channelEpgPrograms.map {
                it.toEpgProgramEntity()
            }
        epgProgramDao.insertPrograms(data = programs)
    }
}
