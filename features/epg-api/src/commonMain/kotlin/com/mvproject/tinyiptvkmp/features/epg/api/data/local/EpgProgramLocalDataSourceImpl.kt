package com.mvproject.tinyiptvkmp.features.epg.api.data.local

import com.mvproject.tinyiptvkmp.features.epg.api.data.local.database.EpgProgramDao
import com.mvproject.tinyiptvkmp.features.epg.api.data.local.database.EpgProgramEntity

internal class EpgProgramLocalDataSourceImpl(
    private val epgProgramDao: EpgProgramDao,
) : EpgProgramLocalDataSource {
    override suspend fun getPrograms(
        channelIds: List<String>,
        time: Long,
    ): List<EpgProgramEntity> =
        epgProgramDao.getPrograms(ids = channelIds, time = time)

    override suspend fun getProgram(
        channelId: String,
        time: Long,
    ): List<EpgProgramEntity> =
        epgProgramDao.getProgram(id = channelId, time = time)

    override suspend fun cleanProgramsBeforeDate(date: Long): Int =
        epgProgramDao.deleteProgramsByDate(timeStamp = date)

    override suspend fun replacePrograms(
        channelId: String,
        programs: List<EpgProgramEntity>,
    ) {
        epgProgramDao.deleteProgram(id = channelId)
        epgProgramDao.insertPrograms(data = programs)
    }
}
