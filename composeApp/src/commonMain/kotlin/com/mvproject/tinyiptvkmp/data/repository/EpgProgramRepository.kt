/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 17.05.24, 18:15
 *
 */

package com.mvproject.tinyiptvkmp.data.repository

import androidx.room.Transaction
import com.mvproject.tinyiptvkmp.core.database.db.AppDatabase
import com.mvproject.tinyiptvkmp.core.network.data.response.EpgProgramResponse
import com.mvproject.tinyiptvkmp.data.mappers.EntityMapper.toEpgProgram
import com.mvproject.tinyiptvkmp.data.mappers.Mapper.asProgramEntity
import com.mvproject.tinyiptvkmp.data.model.epg.EpgProgram
import com.mvproject.tinyiptvkmp.utils.KLog

class EpgProgramRepository(
    private val appDatabase: AppDatabase,
) {
    private val epgProgramDao = appDatabase.epgProgramDao()

    suspend fun getEpgProgramsByIds(
        channelIds: List<String>,
        time: Long,
    ): List<EpgProgram> =
        epgProgramDao
            .getPrograms(ids = channelIds, time = time)
            .map { it.toEpgProgram() }

    suspend fun getEpgProgramsById(
        channelId: String,
        time: Long,
    ): List<EpgProgram> =
        epgProgramDao
            .getProgram(id = channelId, time = time)
            .map { it.toEpgProgram() }

    suspend fun cleanProgramsBeforeDate(date: Long) {
        val deleted = epgProgramDao.deleteProgramsByDate(timeStamp = date)
        KLog.e("testing cleanProgramsBeforeDate deleted=$deleted")
    }

    @Transaction
    suspend fun updatePrograms(
        channelId: String,
        programs: List<EpgProgramResponse>,
    ) {
        val entities =
            programs.map { item ->
                item.asProgramEntity(id = channelId)
            }

        epgProgramDao.apply {
            deleteProgram(id = channelId)
            insertPrograms(data = entities)
        }
    }
}
