/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:15
 *
 */

package com.mvproject.tinyiptv.data.repository

import com.mvproject.tinyiptv.VideoAppDatabase
import com.mvproject.tinyiptv.data.mappers.EntityMapper.toEpgProgram
import com.mvproject.tinyiptv.data.mappers.EntityMapper.toEpgProgramEntity
import com.mvproject.tinyiptv.data.models.epg.EpgProgram
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EpgProgramRepository(private val db: VideoAppDatabase) {

    private val epgProgramQueries = db.epgProgramEntityQueries

    fun getEpgProgramsById(channelId: String, time: Long): List<EpgProgram> {
        return epgProgramQueries.getEpgProgramsById(id = channelId, time = time)
            .executeAsList()
            .map { entity ->
                entity.toEpgProgram()
            }
    }

    fun getEpgProgramsByIdWithLimit(channelId: String, limit: Long, time: Long): List<EpgProgram> {
        return epgProgramQueries.getEpgProgramsByIdWithLimit(
            id = channelId,
            time = time,
            limit = limit
        ).executeAsList()
            .map { entity ->
                entity.toEpgProgram()
            }
    }

    fun getEpgProgramsByIds(channelIds: List<String>, time: Long): List<EpgProgram> {
        return epgProgramQueries.getEpgProgramsByIds(ids = channelIds, time = time)
            .executeAsList()
            .map { entity ->
                entity.toEpgProgram()
            }
    }

    // fun getEpgPrograms(): List<EpgProgram> {
    //     return queries.getEpgPrograms().executeAsList().map {
    //         it.toEpgProgram()
    //     }
    // }


    suspend fun deleteEpgProgramsByIdTime(id: String, time: Long) {
        withContext(Dispatchers.IO) {
            epgProgramQueries.transaction {
                epgProgramQueries.deleteEpgProgramsByIdTime(
                    id = id,
                    time = time
                )
            }
        }
    }

    suspend fun insertEpgProgram(channel: EpgProgram) {
        withContext(Dispatchers.IO) {
            epgProgramQueries.insertEpgProgram(
                channel.toEpgProgramEntity()
            )
        }
    }

    fun insertEpgProgram2(channel: EpgProgram) {
        epgProgramQueries.transaction {
            epgProgramQueries.deleteEpgProgramsForChannel(id = channel.channelId)
            epgProgramQueries.insertEpgProgram(
                channel.toEpgProgramEntity()
            )
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