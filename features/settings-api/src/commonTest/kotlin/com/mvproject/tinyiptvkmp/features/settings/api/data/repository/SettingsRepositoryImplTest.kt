package com.mvproject.tinyiptvkmp.features.settings.api.data.repository

import com.mvproject.tinyiptvkmp.core.datastore.preferences.AppPreferencesProto
import com.mvproject.tinyiptvkmp.core.foundation.model.ChannelsViewType
import com.mvproject.tinyiptvkmp.features.settings.api.data.local.SettingsLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SettingsRepositoryImplTest {
    @Test
    fun mapsAndUpdatesStoredSettings() = runTest {
        val storedPreferences = MutableStateFlow(AppPreferencesProto())
        val repository = SettingsRepositoryImpl(FakeSettingsLocalDataSource(storedPreferences))

        repository.updateInfoUpdatePeriod(10)
        repository.updateEpgUpdatePeriod(20)
        repository.updateChannelsViewType(ChannelsViewType.GRID)
        repository.updateFullscreenMode(true)
        repository.updateVideoSize(3)

        assertEquals(10, repository.observeGeneralSettings().first().infoUpdatePeriod)
        assertEquals(20, repository.observeGeneralSettings().first().epgUpdatePeriod)
        assertEquals(
            ChannelsViewType.GRID,
            repository.observeGeneralSettings().first().channelsViewType
        )
        assertEquals(true, repository.observePlayerSettings().first().isFullscreenEnabled)
        assertEquals(3, repository.observePlayerSettings().first().videoSize)
    }
}

private class FakeSettingsLocalDataSource(
    private val storedPreferences: MutableStateFlow<AppPreferencesProto>,
) : SettingsLocalDataSource {
    override val preferences: Flow<AppPreferencesProto> = storedPreferences

    override val channelsEpgInfoUpdateRequired: Flow<Boolean> =
        storedPreferences.map { preferences -> preferences.channelsEpgInfoUpdateRequired }

    override suspend fun update(transform: (AppPreferencesProto) -> AppPreferencesProto) {
        storedPreferences.value = transform(storedPreferences.value)
    }
}
