package com.mvproject.tinyiptvkmp.features.player.api.data.repository

import com.mvproject.tinyiptvkmp.features.player.api.data.local.PlayerLocalDataSource
import com.mvproject.tinyiptvkmp.features.player.api.data.local.PlayerPreferencesProto
import com.mvproject.tinyiptvkmp.features.player.api.domain.model.PlayerSettings
import com.mvproject.tinyiptvkmp.features.player.api.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class PlayerRepositoryImpl(
    private val localDataSource: PlayerLocalDataSource,
) : PlayerRepository {
    override fun observePlayerSettings(): Flow<PlayerSettings> =
        localDataSource.preferences.map { preferences -> preferences.toPlayerSettings() }

    override suspend fun updateFullscreenMode(enabled: Boolean) {
        localDataSource.update { it.copy(isFullscreenEnabled = enabled) }
    }

    override suspend fun updateVideoSize(mode: Int) {
        localDataSource.update { it.copy(videoSize = mode) }
    }
}

private fun PlayerPreferencesProto.toPlayerSettings(): PlayerSettings =
    PlayerSettings(
        isFullscreenEnabled = isFullscreenEnabled,
        videoSize = videoSize,
    )
