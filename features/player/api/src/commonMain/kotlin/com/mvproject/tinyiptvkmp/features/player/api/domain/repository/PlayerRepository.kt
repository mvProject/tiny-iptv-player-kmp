package com.mvproject.tinyiptvkmp.features.player.api.domain.repository

import com.mvproject.tinyiptvkmp.core.foundation.model.VideoSize
import com.mvproject.tinyiptvkmp.features.player.api.domain.model.PlayerSettings
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    fun observePlayerSettings(): Flow<PlayerSettings>

    suspend fun getPlayerSettings(): PlayerSettings

    suspend fun updateFullscreenMode(enabled: Boolean)

    suspend fun updateVideoSize(mode: VideoSize)
}
