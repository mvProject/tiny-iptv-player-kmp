package com.mvproject.tinyiptvkmp.features.epg.api.domain.repository

import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgProgram

interface EpgProgramRepository {
    suspend fun getEpgProgramsByIds(
        channelIds: List<String>,
        time: Long,
    ): List<EpgProgram>

    suspend fun getEpgProgramsById(
        channelId: String,
        time: Long,
    ): List<EpgProgram>

    suspend fun cleanProgramsBeforeDate(date: Long)

    suspend fun updateProgramsFromSource(
        sourceUrl: String,
        currentDate: Long,
        onChannelProgramsUpdated: () -> Unit,
    ): Int
}
