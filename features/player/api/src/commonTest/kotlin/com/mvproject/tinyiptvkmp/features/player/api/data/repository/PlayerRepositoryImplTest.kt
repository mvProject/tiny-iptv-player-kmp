package com.mvproject.tinyiptvkmp.features.player.api.data.repository

import com.mvproject.tinyiptvkmp.features.player.api.data.storage.PlayerLocalDataSource
import com.mvproject.tinyiptvkmp.features.player.api.data.storage.PlayerPreferencesProto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class PlayerRepositoryImplTest {
    @Test
    fun mapsStoredPlayerSettings() =
        runTest {
            val repository =
                PlayerRepositoryImpl(
                    FakePlayerLocalDataSource(
                        MutableStateFlow(
                            PlayerPreferencesProto(
                                isFullscreenEnabled = true,
                                videoSize = 2,
                            ),
                        ),
                    ),
                )

            val settings = repository.observePlayerSettings().first()

            assertEquals(true, settings.isFullscreenEnabled)
            assertEquals(2, settings.videoSize)
        }

    @Test
    fun updatesStoredPlayerSettings() =
        runTest {
            val storedPreferences = MutableStateFlow(PlayerPreferencesProto())
            val repository = PlayerRepositoryImpl(FakePlayerLocalDataSource(storedPreferences))

            repository.updateFullscreenMode(true)
            repository.updateVideoSize(3)

            val settings = repository.observePlayerSettings().first()
            assertEquals(true, settings.isFullscreenEnabled)
            assertEquals(3, settings.videoSize)
        }
}

private class FakePlayerLocalDataSource(
    private val storedPreferences: MutableStateFlow<PlayerPreferencesProto>,
) : PlayerLocalDataSource {
    override val preferences: Flow<PlayerPreferencesProto> = storedPreferences

    override suspend fun update(transform: (PlayerPreferencesProto) -> PlayerPreferencesProto) {
        storedPreferences.value = transform(storedPreferences.value)
    }
}
