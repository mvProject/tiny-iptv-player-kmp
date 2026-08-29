package com.mvproject.tinyiptvkmp.features.playlist.api.data.repository

import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import com.mvproject.tinyiptvkmp.core.datastore.preferences.AppPreferencesProto
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistSyncStateRepository

internal class PlaylistSyncStateRepositoryImpl(
    private val preferencesStore: ProtoStore<AppPreferencesProto>,
) : PlaylistSyncStateRepository {
    override suspend fun markChannelsEpgInfoUpdateRequired() {
        preferencesStore.update { preferences ->
            preferences.copy(channelsEpgInfoUpdateRequired = true)
        }
    }
}
