package com.mvproject.tinyiptvkmp.features.playlist.api

import com.mvproject.tinyiptvkmp.features.playlist.api.domain.util.playlistUpdatePeriodToDuration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

class PlaylistUpdatePeriodTest {
    @Test
    fun mapsUiPeriodValuesToDurations() {
        assertEquals(0L, playlistUpdatePeriodToDuration(0))
        assertEquals(6.hours.inWholeMilliseconds, playlistUpdatePeriodToDuration(1))
        assertEquals(12.hours.inWholeMilliseconds, playlistUpdatePeriodToDuration(2))
        assertEquals(24.hours.inWholeMilliseconds, playlistUpdatePeriodToDuration(3))
        assertEquals(2.days.inWholeMilliseconds, playlistUpdatePeriodToDuration(4))
        assertEquals(7.days.inWholeMilliseconds, playlistUpdatePeriodToDuration(5))
    }

    @Test
    fun mapsUnknownPeriodToNoValue() {
        assertEquals(-1L, playlistUpdatePeriodToDuration(999))
    }
}
