package com.mvproject.tinyiptvkmp.features.settings.api.data.local

import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import com.mvproject.tinyiptvkmp.core.datastore.preferences.AppPreferencesProto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal interface SettingsLocalDataSource {
    val preferences: Flow<AppPreferencesProto>

    val channelsEpgInfoUpdateRequired: Flow<Boolean>

    suspend fun update(transform: (AppPreferencesProto) -> AppPreferencesProto)
}

internal class SettingsLocalDataSourceImpl(
    private val preferencesStore: ProtoStore<AppPreferencesProto>,
) : SettingsLocalDataSource {
    override val preferences: Flow<AppPreferencesProto> = preferencesStore.data

    override val channelsEpgInfoUpdateRequired: Flow<Boolean> =
        preferencesStore.data.map { preferences -> preferences.channelsEpgInfoUpdateRequired }

    override suspend fun update(transform: (AppPreferencesProto) -> AppPreferencesProto) {
        preferencesStore.update(transform)
    }
}
