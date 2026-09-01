package com.mvproject.tinyiptvkmp.features.epg.api.data.datasource.local

import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgProgram

interface EpgProgramLocalDataSource {
    suspend fun getPrograms(
        channelIds: List<String>,
        time: Long,
    ): List<EpgProgram>

    suspend fun getProgram(
        channelId: String,
        time: Long,
    ): List<EpgProgram>

    suspend fun cleanProgramsBeforeDate(date: Long): Int

    suspend fun replacePrograms(
        channelId: String,
        programs: List<EpgProgram>,
    )
}
