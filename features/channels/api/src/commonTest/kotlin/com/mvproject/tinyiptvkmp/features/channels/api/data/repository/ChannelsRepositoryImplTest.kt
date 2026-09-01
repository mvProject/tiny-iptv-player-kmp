package com.mvproject.tinyiptvkmp.features.channels.api.data.repository

import com.mvproject.tinyiptvkmp.core.foundation.model.ChannelsViewType
import com.mvproject.tinyiptvkmp.features.channels.api.data.storage.ChannelsLocalDataSource
import com.mvproject.tinyiptvkmp.features.channels.api.data.storage.ChannelsPreferencesProto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ChannelsRepositoryImplTest {
    @Test
    fun mapsDefaultChannelSettings() = runTest {
        val repository =
            ChannelsRepositoryImpl(
                FakeChannelsLocalDataSource(MutableStateFlow(ChannelsPreferencesProto())),
            )

        val settings = repository.observeChannelsSettings().first()

        assertEquals(ChannelsViewType.LIST, settings.channelsViewType)
        assertFalse(settings.channelsEpgInfoUpdateRequired)
    }

    @Test
    fun mapsStoredChannelSettings() = runTest {
        val repository =
            ChannelsRepositoryImpl(
                FakeChannelsLocalDataSource(
                    MutableStateFlow(
                        ChannelsPreferencesProto(
                            channelsViewType = "GRID",
                            channelsEpgInfoUpdateRequired = true,
                        ),
                    ),
                ),
            )

        val settings = repository.observeChannelsSettings().first()

        assertEquals(ChannelsViewType.GRID, settings.channelsViewType)
        assertTrue(settings.channelsEpgInfoUpdateRequired)
    }

    @Test
    fun updatesChannelSettings() = runTest {
        val storedPreferences = MutableStateFlow(ChannelsPreferencesProto())
        val repository = ChannelsRepositoryImpl(FakeChannelsLocalDataSource(storedPreferences))

        repository.updateChannelsViewType(ChannelsViewType.CARD)
        repository.markChannelsEpgInfoUpdateRequired()

        assertEquals("CARD", storedPreferences.value.channelsViewType)
        assertTrue(repository.observeChannelsEpgInfoUpdateRequired().first())

        repository.markChannelsEpgInfoUpdated()

        assertFalse(storedPreferences.value.channelsEpgInfoUpdateRequired)
    }
}

private class FakeChannelsLocalDataSource(
    private val storedPreferences: MutableStateFlow<ChannelsPreferencesProto>,
) : ChannelsLocalDataSource {
    override val preferences: Flow<ChannelsPreferencesProto> = storedPreferences

    override val channelsEpgInfoUpdateRequired: Flow<Boolean> =
        storedPreferences.map { preferences -> preferences.channelsEpgInfoUpdateRequired }

    override suspend fun update(transform: (ChannelsPreferencesProto) -> ChannelsPreferencesProto) {
        storedPreferences.value = transform(storedPreferences.value)
    }
}
