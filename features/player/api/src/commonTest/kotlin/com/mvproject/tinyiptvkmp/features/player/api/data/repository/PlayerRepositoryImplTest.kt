package com.mvproject.tinyiptvkmp.features.player.api.data.repository

import com.mvproject.tinyiptvkmp.core.foundation.model.VideoSize
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
            assertEquals(VideoSize.WideScreen, settings.videoSize)
        }

    @Test
    fun getsStoredPlayerSettings() =
        runTest {
            val repository =
                PlayerRepositoryImpl(
                    FakePlayerLocalDataSource(
                        MutableStateFlow(
                            PlayerPreferencesProto(
                                isFullscreenEnabled = true,
                                videoSize = VideoSize.FullScreen.ordinal,
                            ),
                        ),
                    ),
                )

            val settings = repository.getPlayerSettings()

            assertEquals(true, settings.isFullscreenEnabled)
            assertEquals(VideoSize.FullScreen, settings.videoSize)
        }

    @Test
    fun mapsInvalidStoredVideoSizeToDefault() =
        runTest {
            val repository =
                PlayerRepositoryImpl(
                    FakePlayerLocalDataSource(
                        MutableStateFlow(
                            PlayerPreferencesProto(
                                videoSize = Int.MAX_VALUE,
                            ),
                        ),
                    ),
                )

            val settings = repository.getPlayerSettings()

            assertEquals(VideoSize.Cinematic, settings.videoSize)
        }

    @Test
    fun updatesStoredPlayerSettings() =
        runTest {
            val storedPreferences = MutableStateFlow(PlayerPreferencesProto())
            val repository = PlayerRepositoryImpl(FakePlayerLocalDataSource(storedPreferences))

            repository.updateFullscreenMode(true)
            repository.updateVideoSize(VideoSize.FillScreen)

            val settings = repository.observePlayerSettings().first()
            assertEquals(true, settings.isFullscreenEnabled)
            assertEquals(VideoSize.FillScreen, settings.videoSize)
            assertEquals(VideoSize.FillScreen.ordinal, storedPreferences.value.videoSize)
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
