package com.mvproject.tinyiptvkmp.features.channels.api.data.local

import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal interface ChannelsLocalDataSource {
    val preferences: Flow<ChannelsPreferencesProto>

    val channelsEpgInfoUpdateRequired: Flow<Boolean>

    suspend fun update(transform: (ChannelsPreferencesProto) -> ChannelsPreferencesProto)
}

internal class ChannelsLocalDataSourceImpl(
    private val preferencesStore: ProtoStore<ChannelsPreferencesProto>,
) : ChannelsLocalDataSource {
    override val preferences: Flow<ChannelsPreferencesProto> = preferencesStore.data

    override val channelsEpgInfoUpdateRequired: Flow<Boolean> =
        preferencesStore.data.map { preferences -> preferences.channelsEpgInfoUpdateRequired }

    override suspend fun update(transform: (ChannelsPreferencesProto) -> ChannelsPreferencesProto) {
        preferencesStore.update(transform)
    }
}
