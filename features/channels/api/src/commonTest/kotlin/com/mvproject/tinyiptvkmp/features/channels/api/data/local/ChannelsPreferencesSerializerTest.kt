package com.mvproject.tinyiptvkmp.features.channels.api.data.local

import com.mvproject.tinyiptvkmp.features.channels.api.data.storage.ChannelsPreferencesProto
import com.mvproject.tinyiptvkmp.features.channels.api.data.storage.ChannelsPreferencesSerializer
import kotlinx.coroutines.test.runTest
import okio.Buffer
import kotlin.test.Test
import kotlin.test.assertEquals

class ChannelsPreferencesSerializerTest {
    @Test
    fun writeThenReadReturnsPreferences() =
        runTest {
            val expected =
                ChannelsPreferencesProto(
                    channelsViewType = "GRID",
                    channelsEpgInfoUpdateRequired = true,
                )
            val buffer = Buffer()

            ChannelsPreferencesSerializer.writeTo(expected, buffer)
            val actual = ChannelsPreferencesSerializer.readFrom(buffer)

            assertEquals(expected, actual)
        }

    @Test
    fun readFromInvalidBytesReturnsDefaultPreferences() =
        runTest {
            val buffer = Buffer().writeUtf8("not a proto payload")

            val actual = ChannelsPreferencesSerializer.readFrom(buffer)

            assertEquals(ChannelsPreferencesSerializer.defaultValue, actual)
        }
}
