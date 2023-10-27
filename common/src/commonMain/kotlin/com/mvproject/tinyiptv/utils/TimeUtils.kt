/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 26.10.23, 14:44
 *
 */

package com.mvproject.tinyiptv.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

object TimeUtils {
    val timeZoneSource
        get() = TimeZone.of("Europe/Moscow")
    val timeZoneCurrent
        get() = TimeZone.currentSystemDefault()

    val actualDate
        get() = Clock.System.now().toEpochMilliseconds()

    /**
     * Extension Method to non-null long variable which
     * convert value to specified time with local timezone
     *
     * @return String converted time value
     */
    fun Long.convertTimeToReadableFormat(): String {
        val local = Instant
            .fromEpochMilliseconds(this)
            .toLocalDateTime(timeZoneCurrent)
        return ("${local.hour.asTwoSign()}:${local.minute.asTwoSign()}")
    }

    fun Long.correctTimeZone(): Long {
        val instant = Instant.fromEpochMilliseconds(this)
        return instant.toLocalDateTime(timeZoneCurrent)
            .toInstant(timeZoneSource)
            .toEpochMilliseconds()
    }

    private fun Int.asTwoSign() = if (this < 10) "0${this}" else this.toString()

    fun typeToDuration(type: Int): Long =
        when (type) {
            0 -> AppConstants.LONG_VALUE_ZERO
            1 -> 6.hours.inWholeMilliseconds
            2 -> 12.hours.inWholeMilliseconds
            3 -> 24.hours.inWholeMilliseconds
            4 -> 2.days.inWholeMilliseconds
            5 -> 7.days.inWholeMilliseconds
            else -> AppConstants.LONG_NO_VALUE
        }

    fun calculateProgramProgress(startTime: Long, endTime: Long): Float {
        var progressValue = 0f
        val currTime = System.currentTimeMillis()
        if (currTime > startTime) {
            val endValue = (endTime - startTime).toInt()
            val spendValue = (currTime - startTime).toDouble()
            progressValue = (spendValue / endValue).toFloat()
        }
        return progressValue
    }

    fun Boolean.toLong() = if (this) 1L else 0L
    fun Long.toBoolean() = this != 0L
}