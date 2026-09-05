package com.mvproject.tinyiptvkmp.features.player.api.data.repository

import com.mvproject.tinyiptvkmp.core.foundation.model.VideoSize
import com.mvproject.tinyiptvkmp.features.player.api.data.storage.PlayerLocalDataSource
import com.mvproject.tinyiptvkmp.features.player.api.data.storage.PlayerPreferencesProto
import com.mvproject.tinyiptvkmp.features.player.api.domain.model.PlayerSettings
import com.mvproject.tinyiptvkmp.features.player.api.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val DEFAULT_VIDEO_SIZE =
    VideoSize.entries.getOrNull(PlayerPreferencesProto().videoSize) ?: VideoSize.Cinematic

internal class PlayerRepositoryImpl(
    private val localDataSource: PlayerLocalDataSource,
) : PlayerRepository {
    override fun observePlayerSettings(): Flow<PlayerSettings> =
        localDataSource.preferences
            .map { preferences -> preferences.toPlayerSettings() }
            .distinctUntilChanged()

    override suspend fun getPlayerSettings(): PlayerSettings =
        localDataSource.preferences.first().toPlayerSettings()

    override suspend fun updateFullscreenMode(enabled: Boolean) {
        localDataSource.update { it.copy(isFullscreenEnabled = enabled) }
    }

    override suspend fun updateVideoSize(mode: VideoSize) {
        localDataSource.update { it.copy(videoSize = mode.ordinal) }
    }
}

private fun PlayerPreferencesProto.toPlayerSettings(): PlayerSettings =
    PlayerSettings(
        isFullscreenEnabled = isFullscreenEnabled,
        videoSize = videoSize.toVideoSize(),
    )

private fun Int.toVideoSize(): VideoSize =
    VideoSize.entries.getOrNull(this) ?: DEFAULT_VIDEO_SIZE
