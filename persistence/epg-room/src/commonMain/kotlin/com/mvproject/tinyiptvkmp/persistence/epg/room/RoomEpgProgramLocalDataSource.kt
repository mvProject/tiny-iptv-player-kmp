package com.mvproject.tinyiptvkmp.persistence.epg.room

import com.mvproject.tinyiptvkmp.features.epg.api.data.datasource.local.EpgProgramLocalDataSource
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgProgram
import com.mvproject.tinyiptvkmp.persistence.epg.room.database.EpgProgramDao
import com.mvproject.tinyiptvkmp.persistence.epg.room.mapper.toEpgProgram
import com.mvproject.tinyiptvkmp.persistence.epg.room.mapper.toEpgProgramEntity

internal class RoomEpgProgramLocalDataSource(
    private val epgProgramDao: EpgProgramDao,
) : EpgProgramLocalDataSource {
    override suspend fun getPrograms(
        channelIds: List<String>,
        time: Long,
    ): List<EpgProgram> =
        epgProgramDao.getPrograms(ids = channelIds, time = time).map { it.toEpgProgram() }

    override suspend fun getProgram(
        channelId: String,
        time: Long,
    ): List<EpgProgram> =
        epgProgramDao.getProgram(id = channelId, time = time).map { it.toEpgProgram() }

    override suspend fun cleanProgramsBeforeDate(date: Long): Int =
        epgProgramDao.deleteProgramsByDate(timeStamp = date)

    override suspend fun replacePrograms(
        channelId: String,
        programs: List<EpgProgram>,
    ) {
        epgProgramDao.deleteProgram(id = channelId)
        epgProgramDao.insertPrograms(data = programs.map { it.toEpgProgramEntity() })
    }
}


