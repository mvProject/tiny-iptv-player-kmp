package com.mvproject.tinyiptvkmp.features.epg.api.data.local

import com.mvproject.tinyiptvkmp.features.epg.api.data.local.database.EpgChannelEntity
import kotlinx.coroutines.flow.Flow

internal interface EpgChannelLocalDataSource {
    suspend fun loadEpgInfoData(): List<EpgChannelEntity>

    fun loadEpgChannels(): Flow<List<EpgChannelEntity>>

    suspend fun replaceChannels(channels: List<EpgChannelEntity>)
}
