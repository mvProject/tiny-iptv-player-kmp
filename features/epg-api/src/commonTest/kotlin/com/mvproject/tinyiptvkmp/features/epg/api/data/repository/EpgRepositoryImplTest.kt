package com.mvproject.tinyiptvkmp.features.epg.api.data.repository

import com.mvproject.tinyiptvkmp.features.epg.api.data.local.EpgPreferencesProto
import com.mvproject.tinyiptvkmp.features.epg.api.data.local.EpgSettingsLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class EpgRepositoryImplTest {
    @Test
    fun mapsStoredEpgSettings() = runTest {
        val repository =
            EpgRepositoryImpl(
                FakeEpgSettingsLocalDataSource(
                    MutableStateFlow(
                        EpgPreferencesProto(
                            epgDataLastUpdate = 100L,
                            epgInfoLastUpdatePeriod = 1,
                            epgMainLastUpdatePeriod = 2,
                            epgInfoDataLastUpdate = 200L,
                            epgProgramClean = 300L,
                        ),
                    ),
                ),
            )

        val settings = repository.getEpgSettings()

        assertEquals(200L, settings.epgInfoDataLastUpdate)
        assertEquals(1, settings.epgInfoUpdatePeriod)
        assertEquals(100L, settings.epgDataLastUpdate)
        assertEquals(2, settings.epgUpdatePeriod)
        assertEquals(300L, settings.epgProgramClean)
    }

    @Test
    fun updatesEpgSettings() = runTest {
        val storedPreferences = MutableStateFlow(EpgPreferencesProto())
        val repository = EpgRepositoryImpl(FakeEpgSettingsLocalDataSource(storedPreferences))

        repository.updateInfoUpdatePeriod(period = 10)
        repository.updateEpgUpdatePeriod(period = 20)
        repository.markEpgChannelsRefreshed(lastUpdate = 100L)
        repository.markEpgProgramsRefreshed(lastUpdate = 200L)
        repository.markEpgProgramsCleaned(cleanedAt = 300L)

        val preferences = storedPreferences.value
        assertEquals(10, repository.observeEpgSettings().first().epgInfoUpdatePeriod)
        assertEquals(20, repository.observeEpgSettings().first().epgUpdatePeriod)
        assertEquals(100L, preferences.epgInfoDataLastUpdate)
        assertEquals(200L, preferences.epgDataLastUpdate)
        assertEquals(300L, preferences.epgProgramClean)
    }
}

private class FakeEpgSettingsLocalDataSource(
    private val storedPreferences: MutableStateFlow<EpgPreferencesProto>,
) : EpgSettingsLocalDataSource {
    override val preferences: Flow<EpgPreferencesProto> = storedPreferences

    override suspend fun update(transform: (EpgPreferencesProto) -> EpgPreferencesProto) {
        storedPreferences.value = transform(storedPreferences.value)
    }
}
