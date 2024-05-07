/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:29
 *
 */

package com.mvproject.tinyiptvkmp.data.repository

import androidx.room.Transaction
import com.mvproject.tinyiptvkmp.data.mappers.EntityMapper.toEpgProgram
import com.mvproject.tinyiptvkmp.data.mappers.EntityMapper.toEpgProgramEntity
import com.mvproject.tinyiptvkmp.data.model.epg.EpgProgram
import com.mvproject.tinyiptvkmp.database.AppDatabase

class EpgProgramRepository(
    // private val db: TinyIptvKmpDatabase,
    private val appDatabase: AppDatabase,
) {
    //  private val epgProgramQueries = db.epgProgramEntityQueries
    private val epgProgramDao = appDatabase.epgProgramDao()

    /*    fun getEpgProgramsByIds(
            channelIds: List<String>,
            time: Long,
        ): List<EpgProgram> {
            return epgProgramQueries.getEpgProgramsByIds(ids = channelIds, time = time)
                .executeAsList()
                .map { entity ->
                    entity.toEpgProgram()
                }
        }*/

    suspend fun getEpgProgramsByIds(
        channelIds: List<String>,
        time: Long,
    ): List<EpgProgram> {
        return epgProgramDao.getPrograms(ids = channelIds, time = time)
            .map {
                it.toEpgProgram()
            }
    }

    /*    suspend fun insertEpgPrograms(
            channelId: String,
            channelEpgPrograms: List<EpgProgram>,
        ) {
            withContext(Dispatchers.IO) {
                epgProgramQueries.transaction {
                    epgProgramQueries.deleteEpgProgramsForChannel(id = channelId)

                    channelEpgPrograms.forEach { prg ->
                        epgProgramQueries.insertEpgProgram(
                            prg.toEpgProgramEntity(),
                        )
                    }
                }
            }
        }*/

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
