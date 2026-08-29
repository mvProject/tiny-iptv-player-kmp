package com.mvproject.tinyiptvkmp.features.settings.api.data.local

import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import com.mvproject.tinyiptvkmp.core.datastore.preferences.AppPreferencesProto
import kotlinx.coroutines.flow.Flow

internal interface SettingsLocalDataSource {
    val preferences: Flow<AppPreferencesProto>

    suspend fun update(transform: (AppPreferencesProto) -> AppPreferencesProto)
}

internal class SettingsLocalDataSourceImpl(
    private val preferencesStore: ProtoStore<AppPreferencesProto>,
) : SettingsLocalDataSource {
    override val preferences: Flow<AppPreferencesProto> = preferencesStore.data

    override suspend fun update(transform: (AppPreferencesProto) -> AppPreferencesProto) {
        preferencesStore.update(transform)
    }
}
