package com.mvproject.tinyiptvkmp.core.datastore.preferences

import kotlinx.coroutines.test.runTest
import okio.Buffer
import kotlin.test.Test
import kotlin.test.assertEquals

class AppPreferencesSerializerTest {
    @Test
    fun writeThenReadReturnsPreferences() =
        runTest {
            val expected =
                AppPreferencesProto(
                    channelsViewType = "GRID",
                    epgDataLastUpdate = 123L,
                    epgInfoLastUpdatePeriod = 2,
                    epgMainLastUpdatePeriod = 3,
                    defaultVideoSizeMode = 1,
                    defaultFullscreenMode = true,
                    epgInfoDataLastUpdate = 456L,
                    channelsEpgInfoUpdateRequired = true,
                    playlistContentLoadRequired = "playlist-id",
                    epgProgramClean = 789L,
                )
            val buffer = Buffer()

            AppPreferencesSerializer.writeTo(expected, buffer)
            val actual = AppPreferencesSerializer.readFrom(buffer)

            assertEquals(expected, actual)
        }

    @Test
    fun readFromInvalidBytesReturnsDefaultPreferences() =
        runTest {
            val buffer = Buffer().writeUtf8("not a proto payload")

            val actual = AppPreferencesSerializer.readFrom(buffer)

            assertEquals(AppPreferencesSerializer.defaultValue, actual)
        }
}
