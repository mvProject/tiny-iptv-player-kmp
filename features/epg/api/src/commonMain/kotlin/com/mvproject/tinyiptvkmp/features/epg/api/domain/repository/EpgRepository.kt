package com.mvproject.tinyiptvkmp.features.epg.api.domain.repository

import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgSettings
import kotlinx.coroutines.flow.Flow

interface EpgRepository {
    fun observeEpgSettings(): Flow<EpgSettings>

    suspend fun getEpgSettings(): EpgSettings

    suspend fun updateInfoUpdatePeriod(period: Int)

    suspend fun updateEpgUpdatePeriod(period: Int)

    suspend fun markEpgChannelsRefreshed(lastUpdate: Long)

    suspend fun markEpgProgramsRefreshed(lastUpdate: Long)

    suspend fun markEpgProgramsCleaned(cleanedAt: Long)
}
