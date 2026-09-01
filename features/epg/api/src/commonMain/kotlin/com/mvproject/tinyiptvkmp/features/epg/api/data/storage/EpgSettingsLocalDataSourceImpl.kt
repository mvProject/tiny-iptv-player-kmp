package com.mvproject.tinyiptvkmp.features.epg.api.data.storage

import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import kotlinx.coroutines.flow.Flow

internal class EpgSettingsLocalDataSourceImpl(
    private val preferencesStore: ProtoStore<EpgPreferencesProto>,
) : EpgSettingsLocalDataSource {
    override val preferences: Flow<EpgPreferencesProto> = preferencesStore.data

    override suspend fun update(transform: (EpgPreferencesProto) -> EpgPreferencesProto) {
        preferencesStore.update(transform)
    }
}