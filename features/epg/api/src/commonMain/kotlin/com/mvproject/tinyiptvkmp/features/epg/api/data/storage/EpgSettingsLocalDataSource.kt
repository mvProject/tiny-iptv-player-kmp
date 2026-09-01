package com.mvproject.tinyiptvkmp.features.epg.api.data.storage

import kotlinx.coroutines.flow.Flow

internal interface EpgSettingsLocalDataSource {
    val preferences: Flow<EpgPreferencesProto>

    suspend fun update(transform: (EpgPreferencesProto) -> EpgPreferencesProto)
}