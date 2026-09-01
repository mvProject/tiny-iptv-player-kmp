package com.mvproject.tinyiptvkmp.features.player.api.data.local

import kotlinx.coroutines.test.runTest
import okio.Buffer
import kotlin.test.Test
import kotlin.test.assertEquals

class PlayerPreferencesSerializerTest {
    @Test
    fun writeThenReadReturnsPreferences() =
        runTest {
            val expected =
                PlayerPreferencesProto(
                    isFullscreenEnabled = true,
                    videoSize = 3,
                )
            val buffer = Buffer()

            PlayerPreferencesSerializer.writeTo(expected, buffer)
            val actual = PlayerPreferencesSerializer.readFrom(buffer)

            assertEquals(expected, actual)
        }

    @Test
    fun readFromInvalidBytesReturnsDefaultPreferences() =
        runTest {
            val buffer = Buffer().writeUtf8("not a proto payload")

            val actual = PlayerPreferencesSerializer.readFrom(buffer)

            assertEquals(PlayerPreferencesSerializer.defaultValue, actual)
        }
}
