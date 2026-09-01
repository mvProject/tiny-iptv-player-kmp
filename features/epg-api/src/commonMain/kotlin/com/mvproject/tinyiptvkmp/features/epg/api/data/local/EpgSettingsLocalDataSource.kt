package com.mvproject.tinyiptvkmp.features.epg.api.data.local

import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import kotlinx.coroutines.flow.Flow

internal interface EpgSettingsLocalDataSource {
    val preferences: Flow<EpgPreferencesProto>

    suspend fun update(transform: (EpgPreferencesProto) -> EpgPreferencesProto)
}

internal class EpgSettingsLocalDataSourceImpl(
    private val preferencesStore: ProtoStore<EpgPreferencesProto>,
) : EpgSettingsLocalDataSource {
    override val preferences: Flow<EpgPreferencesProto> = preferencesStore.data

    override suspend fun update(transform: (EpgPreferencesProto) -> EpgPreferencesProto) {
        preferencesStore.update(transform)
    }
}
