package com.mvproject.tinyiptvkmp.features.epg.api.data.local

import kotlinx.coroutines.test.runTest
import okio.Buffer
import kotlin.test.Test
import kotlin.test.assertEquals

class EpgPreferencesSerializerTest {
    @Test
    fun writeThenReadReturnsPreferences() =
        runTest {
            val expected =
                EpgPreferencesProto(
                    epgDataLastUpdate = 100L,
                    epgInfoLastUpdatePeriod = 1,
                    epgMainLastUpdatePeriod = 2,
                    epgInfoDataLastUpdate = 200L,
                    epgProgramClean = 300L,
                )
            val buffer = Buffer()

            EpgPreferencesSerializer.writeTo(expected, buffer)
            val actual = EpgPreferencesSerializer.readFrom(buffer)

            assertEquals(expected, actual)
        }

    @Test
    fun readFromInvalidBytesReturnsDefaultPreferences() =
        runTest {
            val buffer = Buffer().writeUtf8("not a proto payload")

            val actual = EpgPreferencesSerializer.readFrom(buffer)

            assertEquals(EpgPreferencesSerializer.defaultValue, actual)
        }
}
