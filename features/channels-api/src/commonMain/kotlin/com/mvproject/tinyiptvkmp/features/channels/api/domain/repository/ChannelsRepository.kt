package com.mvproject.tinyiptvkmp.features.channels.api.domain.repository

import com.mvproject.tinyiptvkmp.core.foundation.model.ChannelsViewType
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.ChannelsSettings
import kotlinx.coroutines.flow.Flow

interface ChannelsRepository {
    fun observeChannelsSettings(): Flow<ChannelsSettings>

    fun observeChannelsEpgInfoUpdateRequired(): Flow<Boolean>

    suspend fun markChannelsEpgInfoUpdateRequired()

    suspend fun markChannelsEpgInfoUpdated()

    suspend fun updateChannelsViewType(type: ChannelsViewType)
}
