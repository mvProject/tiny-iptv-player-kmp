package com.mvproject.tinyiptvkmp.features.playlist.api.domain.util

import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

internal fun playlistUpdatePeriodToDuration(period: Int): Long =
    when (period) {
        0 -> 0L
        1 -> 6.hours.inWholeMilliseconds
        2 -> 12.hours.inWholeMilliseconds
        3 -> 24.hours.inWholeMilliseconds
        4 -> 2.days.inWholeMilliseconds
        5 -> 7.days.inWholeMilliseconds
        else -> -1L
    }
