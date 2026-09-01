package com.mvproject.tinyiptvkmp.features.player.api.domain.repository

import com.mvproject.tinyiptvkmp.features.player.api.domain.model.PlayerSettings
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    fun observePlayerSettings(): Flow<PlayerSettings>

    suspend fun updateFullscreenMode(enabled: Boolean)

    suspend fun updateVideoSize(mode: Int)
}
