package com.mvproject.tinyiptvkmp.features.channels.api.data.storage

import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


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