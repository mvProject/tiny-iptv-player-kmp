package com.mvproject.tinyiptvkmp.features.channels.api.data.repository

import com.mvproject.tinyiptvkmp.core.foundation.model.ChannelsViewType
import com.mvproject.tinyiptvkmp.core.foundation.model.ChannelsViewType.Companion.mapViewType
import com.mvproject.tinyiptvkmp.features.channels.api.data.storage.ChannelsLocalDataSource
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.ChannelsSettings
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ChannelsRepositoryImpl(
    private val localDataSource: ChannelsLocalDataSource,
) : ChannelsRepository {
    override fun observeChannelsSettings(): Flow<ChannelsSettings> =
        localDataSource.preferences.map { preferences ->
            ChannelsSettings(
                channelsEpgInfoUpdateRequired = preferences.channelsEpgInfoUpdateRequired,
                channelsViewType = preferences.channelsViewType.mapViewType(),
            )
        }

    override fun observeChannelsEpgInfoUpdateRequired(): Flow<Boolean> =
        localDataSource.channelsEpgInfoUpdateRequired

    override suspend fun markChannelsEpgInfoUpdateRequired() {
        localDataSource.update { preferences ->
            preferences.copy(channelsEpgInfoUpdateRequired = true)
        }
    }

    override suspend fun markChannelsEpgInfoUpdated() {
        localDataSource.update { preferences ->
            preferences.copy(channelsEpgInfoUpdateRequired = false)
        }
    }

    override suspend fun updateChannelsViewType(type: ChannelsViewType) {
        localDataSource.update { preferences ->
            preferences.copy(channelsViewType = type.name)
        }
    }
}
