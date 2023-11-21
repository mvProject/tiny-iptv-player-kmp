/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:15
 *
 */

package com.mvproject.tinyiptvkmp.data.repository

import com.mvproject.tinyiptvkmp.TinyIptvKmpDatabase
import com.mvproject.tinyiptvkmp.data.mappers.EntityMapper.toEpgProgram
import com.mvproject.tinyiptvkmp.data.mappers.EntityMapper.toEpgProgramEntity
import com.mvproject.tinyiptvkmp.data.model.epg.EpgProgram
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EpgProgramRepository(private val db: TinyIptvKmpDatabase) {

    private val epgProgramQueries = db.epgProgramEntityQueries

    fun getEpgProgramsByIds(channelIds: List<String>, time: Long): List<EpgProgram> {
        return epgProgramQueries.getEpgProgramsByIds(ids = channelIds, time = time)
            .executeAsList()
            .map { entity ->
                entity.toEpgProgram()
            }
    }

    suspend fun insertEpgPrograms(
        channelId: String,
        channelEpgPrograms: List<EpgProgram>
    ) {
        withContext(Dispatchers.IO) {
            epgProgramQueries.transaction {
                epgProgramQueries.deleteEpgProgramsForChannel(id = channelId)

                channelEpgPrograms.forEach { prg ->
                    epgProgramQueries.insertEpgProgram(
                        prg.toEpgProgramEntity()
                    )
                }
            }
        }
    }
}