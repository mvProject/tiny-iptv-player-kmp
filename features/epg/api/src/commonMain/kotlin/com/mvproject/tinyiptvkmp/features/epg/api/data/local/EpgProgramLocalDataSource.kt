package com.mvproject.tinyiptvkmp.features.epg.api.data.local

import com.mvproject.tinyiptvkmp.features.epg.api.data.local.database.EpgProgramEntity

internal interface EpgProgramLocalDataSource {
    suspend fun getPrograms(
        channelIds: List<String>,
        time: Long,
    ): List<EpgProgramEntity>

    suspend fun getProgram(
        channelId: String,
        time: Long,
    ): List<EpgProgramEntity>

    suspend fun cleanProgramsBeforeDate(date: Long): Int

    suspend fun replacePrograms(
        channelId: String,
        programs: List<EpgProgramEntity>,
    )
}
