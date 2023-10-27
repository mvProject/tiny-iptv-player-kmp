/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.utils

import com.mvproject.tinyiptv.data.enums.UpdatePeriod
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

object TimeUtils {
    val timeZoneSource
        get() = TimeZone.of("Europe/Moscow")
    /*    val timeZoneCurrent
            get() = TimeZone.currentSystemDefault()*/

    val actualDate
        get() = Clock.System.now().toEpochMilliseconds()

    //**
    // * Extension Method to non-null long variable which
    // * convert value to specified time with local timezone
    // *
    // * @return String converted time value
    //*
    /*    fun Long.convertTimeToReadableFormat(): String {
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
        }*/


    private fun Int.asTwoSign() = if (this < 10) "0${this}" else this.toString()


    fun Boolean.toLong() = if (this) 1L else 0L
    fun Long.toBoolean() = this != 0L


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

    /*



fun ProgramEntity.correctTimeZone(): ProgramEntity {
    val startInstant = Instant.fromEpochMilliseconds(this.dateTimeStart)
    val endInstant = Instant.fromEpochMilliseconds(this.dateTimeEnd)
    return this.copy(
        dateTimeStart = startInstant.toLocalDateTime(tzCurrent)
            .toInstant(tzSource)
            .toEpochMilliseconds(),
        dateTimeEnd = endInstant.toLocalDateTime(tzCurrent)
            .toInstant(tzSource)
            .toEpochMilliseconds()
    )
}*/
}