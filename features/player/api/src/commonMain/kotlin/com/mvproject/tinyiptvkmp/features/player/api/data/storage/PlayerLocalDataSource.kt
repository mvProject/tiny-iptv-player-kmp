package com.mvproject.tinyiptvkmp.features.player.api.data.storage

import kotlinx.coroutines.flow.Flow

internal interface PlayerLocalDataSource {
    val preferences: Flow<PlayerPreferencesProto>

    suspend fun update(transform: (PlayerPreferencesProto) -> PlayerPreferencesProto)
}


