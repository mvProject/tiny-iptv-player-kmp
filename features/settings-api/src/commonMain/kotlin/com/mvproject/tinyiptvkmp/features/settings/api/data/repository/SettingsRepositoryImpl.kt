package com.mvproject.tinyiptvkmp.features.settings.api.data.repository

import com.mvproject.tinyiptvkmp.features.settings.api.data.local.SettingsLocalDataSource
import com.mvproject.tinyiptvkmp.features.settings.api.domain.model.GeneralSettings
import com.mvproject.tinyiptvkmp.features.settings.api.domain.model.PlayerSettings
import com.mvproject.tinyiptvkmp.features.settings.api.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class SettingsRepositoryImpl(
    private val localDataSource: SettingsLocalDataSource,
) : SettingsRepository {
    override fun observeGeneralSettings(): Flow<GeneralSettings> =
        localDataSource.preferences.map { preferences ->
            GeneralSettings(
                infoUpdatePeriod = preferences.epgInfoLastUpdatePeriod,
                epgUpdatePeriod = preferences.epgMainLastUpdatePeriod,
            )
        }

    override fun observePlayerSettings(): Flow<PlayerSettings> =
        localDataSource.preferences.map { preferences ->
            PlayerSettings(
                isFullscreenEnabled = preferences.defaultFullscreenMode,
                videoSize = preferences.defaultVideoSizeMode,
            )
        }

    override suspend fun updateInfoUpdatePeriod(period: Int) {
        localDataSource.update { it.copy(epgInfoLastUpdatePeriod = period) }
    }

    override suspend fun updateEpgUpdatePeriod(period: Int) {
        localDataSource.update { it.copy(epgMainLastUpdatePeriod = period) }
    }

    override suspend fun updateFullscreenMode(enabled: Boolean) {
        localDataSource.update { it.copy(defaultFullscreenMode = enabled) }
    }

    override suspend fun updateVideoSize(mode: Int) {
        localDataSource.update { it.copy(defaultVideoSizeMode = mode) }
    }
}
