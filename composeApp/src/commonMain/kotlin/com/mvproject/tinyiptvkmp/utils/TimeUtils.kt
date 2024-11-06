/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.utils

import com.mvproject.tinyiptvkmp.core.database.entity.EpgProgramEntity
import com.mvproject.tinyiptvkmp.data.enums.UpdatePeriod
import com.mvproject.tinyiptvkmp.utils.CommonUtils.delimiterTime
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds

object TimeUtils {
    private val tzSourceBerlin = TimeZone.of("Europe/Berlin")
    private val tzSourceMoscow = TimeZone.of("Europe/Moscow")
    private val tzCurrent = TimeZone.currentSystemDefault()

    val actualDate
        get() = Clock.System.now().toEpochMilliseconds()

    private val dateFormat =
        LocalDate.Format {
            dayOfMonth()
            char('/')
            monthNumber()
            char('/')
            year()
        }

    private val timeFormat =
        LocalTime.Format {
            hour()
            char(':')
            minute()
        }

    val sourceActualDate
        get() = Instant
            .fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds())
            .toLocalDateTime(tzCurrent)
            .toInstant(tzSourceMoscow)
            .toEpochMilliseconds()


    /**
     * Extension Method to non-null long variable which
     * convert value to specified time with local timezone
     *
     * @return String converted time value
     */
    fun Long.convertTimeToReadableFormat(): String {
        return Instant
            .fromEpochMilliseconds(this)
            .toLocalDateTime(tzCurrent)
            .time
            .let { timeFormat.format(it) }
    }

    fun EpgProgramEntity.correctTimeZone(): EpgProgramEntity {
        val startInstant = Instant.fromEpochMilliseconds(this.dateTimeStart)
        val endInstant = Instant.fromEpochMilliseconds(this.dateTimeEnd)

        val updatedDateTimeStart =
            startInstant
                .toLocalDateTime(tzCurrent)
                .toInstant(tzSourceMoscow)
                .toEpochMilliseconds()

        val updatedDateTimeEnd =
            endInstant
                .toLocalDateTime(tzCurrent)
                .toInstant(tzSourceMoscow)
                .toEpochMilliseconds()

        return this.copy(
            dateTimeStart =
            updatedDateTimeStart,
            dateTimeEnd =
            updatedDateTimeEnd,
        )
    }

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

    fun calculateProgramProgress(
        startTime: Long,
        endTime: Long,
    ): Float {
        var progressValue = 0f
        val currTime = Clock.System.now().toEpochMilliseconds()
        if (currTime > startTime) {
            val endValue = (endTime - startTime).toInt()
            val spendValue = (currTime - startTime).toDouble()
            progressValue = (spendValue / endValue).toFloat()
        }
        return progressValue
    }

    private fun extractDate(input: String): String {
        val year = input.substring(0, 4).toInt()
        val month = input.substring(4, 6).toInt()
        val day = input.substring(6, 8).toInt()
        return String.format("%02d/%02d/%04d", day, month, year)
    }

    private fun extractTime(input: String): String {
        val hour = input.substring(8, 10).toInt()
        val minute = input.substring(10, 12).toInt()
        return String.format("%02d:%02d", hour, minute)
    }

    private fun roundTimeString(time: String): String {
        val (hour, minute) = time.split(String.delimiterTime).map { it.toInt() }
        val lastDigit = minute % 10
        if (hour > 23) {
            throw IllegalArgumentException()
        } else {
            if (minute >= 57) {
                return String.format(
                    "%02d:00",
                    (hour + 1) % 24,
                ) // Edge case for 57, 58, and 59 minutes
            }

            val roundedMinute =
                when (lastDigit) {
                    in 1..3 -> (minute / 10) * 10 // Rounds down (21 -> 20)
                    in 4..6 -> ((minute / 10) * 10) + 5 // Rounds to 5 (24, 26 -> 25)
                    in 7..9 -> ((minute / 10) + 1) * 10 // Rounds up (27, 29 -> 30)
                    else -> minute // If the last digit is 0, keep it unchanged
                }

            // Ensure the rounded minute is formatted properly with leading zero if needed
            return String.format("%02d:%02d", hour, roundedMinute)
        }
    }

    fun parseToInstant(input: String): Long {
        val date = extractDate(input)
        val time = extractTime(input)

        val localDate = dateFormat.parse(date)
        val localTime = timeFormat.parse(roundTimeString(time = time))

        val localDateTime = LocalDateTime(localDate, localTime)
        return localDateTime.toInstant(tzSourceMoscow).toEpochMilliseconds()
    }

    fun calculateDuration(
        start: Long,
        end: Long
    ): Pair<Long, Long> {
        val duration = (end - start).milliseconds
        val hours = duration.inWholeHours
        val minutes = duration.inWholeMinutes % 60

        return Pair(hours, minutes)
    }

    fun Long.convertToTime(): Pair<String, String> {
        val local =
            Instant
                .fromEpochMilliseconds(this)
                .toLocalDateTime(tzCurrent)

        val hour = String.format("%02d", local.hour)
        val minute = String.format("%02d", local.minute)
        return Pair(hour, minute)
    }
}
