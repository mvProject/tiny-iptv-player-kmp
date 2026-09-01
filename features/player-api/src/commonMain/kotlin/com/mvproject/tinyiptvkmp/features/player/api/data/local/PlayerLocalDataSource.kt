package com.mvproject.tinyiptvkmp.features.player.api.data.local

import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import kotlinx.coroutines.flow.Flow

internal interface PlayerLocalDataSource {
    val preferences: Flow<PlayerPreferencesProto>

    suspend fun update(transform: (PlayerPreferencesProto) -> PlayerPreferencesProto)
}

internal class PlayerLocalDataSourceImpl(
    private val preferencesStore: ProtoStore<PlayerPreferencesProto>,
) : PlayerLocalDataSource {
    override val preferences: Flow<PlayerPreferencesProto> = preferencesStore.data

    override suspend fun update(transform: (PlayerPreferencesProto) -> PlayerPreferencesProto) {
        preferencesStore.update(transform)
    }
}
