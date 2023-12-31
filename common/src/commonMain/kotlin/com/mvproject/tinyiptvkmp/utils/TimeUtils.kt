/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.utils

import com.mvproject.tinyiptvkmp.data.enums.UpdatePeriod
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
            UpdatePeriod.NO_UPDATE.value -> AppConstants.LONG_VALUE_ZERO
            UpdatePeriod.HOURS_6.value -> 6.hours.inWholeMilliseconds
            UpdatePeriod.HOURS_12.value -> 12.hours.inWholeMilliseconds
            UpdatePeriod.HOURS_24.value -> 24.hours.inWholeMilliseconds
            UpdatePeriod.DAYS_2.value -> 2.days.inWholeMilliseconds
            UpdatePeriod.WEEK_1.value -> 7.days.inWholeMilliseconds
            else -> AppConstants.LONG_NO_VALUE
        }

    fun calculateProgramProgress(startTime: Long, endTime: Long): Float {
        var progressValue = 0f
        val currTime = Clock.System.now().toEpochMilliseconds()
        if (currTime > startTime) {
            val endValue = (endTime - startTime).toInt()
            val spendValue = (currTime - startTime).toDouble()
            progressValue = (spendValue / endValue).toFloat()
        }
        return progressValue
    }

}