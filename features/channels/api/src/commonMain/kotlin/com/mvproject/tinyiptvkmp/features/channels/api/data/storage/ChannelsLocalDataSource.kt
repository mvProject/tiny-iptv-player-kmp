package com.mvproject.tinyiptvkmp.features.channels.api.data.storage

import kotlinx.coroutines.flow.Flow

internal interface ChannelsLocalDataSource {
    val preferences: Flow<ChannelsPreferencesProto>

    val channelsEpgInfoUpdateRequired: Flow<Boolean>

    suspend fun update(transform: (ChannelsPreferencesProto) -> ChannelsPreferencesProto)
}

