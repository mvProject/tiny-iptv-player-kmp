package com.mvproject.tinyiptvkmp.features.epg.api.data.repository

import com.mvproject.tinyiptvkmp.features.epg.api.data.storage.EpgPreferencesProto
import com.mvproject.tinyiptvkmp.features.epg.api.data.storage.EpgSettingsLocalDataSource
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgSettings
import com.mvproject.tinyiptvkmp.features.epg.api.domain.repository.EpgRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class EpgRepositoryImpl(
    private val localDataSource: EpgSettingsLocalDataSource,
) : EpgRepository {
    override fun observeEpgSettings(): Flow<EpgSettings> =
        localDataSource.preferences.map { preferences ->
            preferences.toEpgSettings()
        }

    override suspend fun getEpgSettings(): EpgSettings =
        localDataSource.preferences.first().toEpgSettings()

    override suspend fun updateInfoUpdatePeriod(period: Int) {
        localDataSource.update { preferences ->
            preferences.copy(epgInfoLastUpdatePeriod = period)
        }
    }

    override suspend fun updateEpgUpdatePeriod(period: Int) {
        localDataSource.update { preferences ->
            preferences.copy(epgMainLastUpdatePeriod = period)
        }
    }

    override suspend fun markEpgChannelsRefreshed(lastUpdate: Long) {
        localDataSource.update { preferences ->
            preferences.copy(epgInfoDataLastUpdate = lastUpdate)
        }
    }

    override suspend fun markEpgProgramsRefreshed(lastUpdate: Long) {
        localDataSource.update { preferences ->
            preferences.copy(epgDataLastUpdate = lastUpdate)
        }
    }

    override suspend fun markEpgProgramsCleaned(cleanedAt: Long) {
        localDataSource.update { preferences ->
            preferences.copy(epgProgramClean = cleanedAt)
        }
    }
}

private fun EpgPreferencesProto.toEpgSettings(): EpgSettings =
    EpgSettings(
        epgInfoDataLastUpdate = epgInfoDataLastUpdate,
        epgInfoUpdatePeriod = epgInfoLastUpdatePeriod,
        epgDataLastUpdate = epgDataLastUpdate,
        epgUpdatePeriod = epgMainLastUpdatePeriod,
        epgProgramClean = epgProgramClean,
    )
