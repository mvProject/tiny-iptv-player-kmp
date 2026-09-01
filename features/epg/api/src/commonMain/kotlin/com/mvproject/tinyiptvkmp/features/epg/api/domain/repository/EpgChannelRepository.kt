package com.mvproject.tinyiptvkmp.features.epg.api.domain.repository

import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgChannel
import kotlinx.coroutines.flow.Flow

interface EpgChannelRepository {
    suspend fun loadEpgInfoData(): List<EpgChannel>

    fun loadEpgChannels(): Flow<List<EpgChannel>>

    suspend fun updateChannelsFromSource(sourceUrl: String)
}
