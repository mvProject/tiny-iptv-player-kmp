package com.mvproject.tinyiptvkmp.features.epg.api.data.datasource.local

import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgChannel
import kotlinx.coroutines.flow.Flow

interface EpgChannelLocalDataSource {
    suspend fun loadEpgInfoData(): List<EpgChannel>

    fun loadEpgChannels(): Flow<List<EpgChannel>>

    suspend fun replaceChannels(channels: List<EpgChannel>)
}
