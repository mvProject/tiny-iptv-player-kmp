package com.mvproject.tinyiptvkmp.features.epg.api.domain.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

private const val longNoValue = -1L
private const val noUpdate = 0
private const val hours6 = 1
private const val hours12 = 2
private const val hours24 = 3
private const val days2 = 4
private const val week1 = 5

private val sourceTimeZone = TimeZone.of("Europe/Moscow")

internal val actualEpgDate
    get() = Clock.System.now().toEpochMilliseconds()

internal fun epgUpdatePeriodToDuration(type: Int): Long =
    when (type) {
        noUpdate -> 0L
        hours6 -> 6.hours.inWholeMilliseconds
        hours12 -> 12.hours.inWholeMilliseconds
        hours24 -> 24.hours.inWholeMilliseconds
        days2 -> 2.days.inWholeMilliseconds
        week1 -> 7.days.inWholeMilliseconds
        else -> longNoValue
    }

private val dateFormat =
    LocalDate.Format {
        day()
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

private fun Int.pad(length: Int): String =
    toString().padStart(length, '0')

private fun extractDate(input: String): String {
    val year = input.substring(0, 4).toInt()
    val month = input.substring(4, 6).toInt()
    val day = input.substring(6, 8).toInt()
    return "${day.pad(2)}/${month.pad(2)}/${year.pad(4)}"
}

private fun extractTime(input: String): String {
    val hour = input.substring(8, 10).toInt()
    val minute = input.substring(10, 12).toInt()
    return "${hour.pad(2)}:${minute.pad(2)}"
}

private fun roundTimeString(time: String): String {
    val (hour, minute) = time.split(":").map { it.toInt() }
    val lastDigit = minute % 10

    if (hour > 23) {
        throw IllegalArgumentException()
    }

    if (minute >= 57) {
        return "${((hour + 1) % 24).pad(2)}:00"
    }

    val roundedMinute =
        when (lastDigit) {
            in 1..3 -> (minute / 10) * 10
            in 4..6 -> ((minute / 10) * 10) + 5
            in 7..9 -> ((minute / 10) + 1) * 10
            else -> minute
        }

    return "${hour.pad(2)}:${roundedMinute.pad(2)}"
}

internal fun parseEpgInstant(input: String): Long {
    val date = extractDate(input)
    val time = extractTime(input)

    val localDate = dateFormat.parse(date)
    val localTime = timeFormat.parse(roundTimeString(time = time))

    return LocalDateTime(localDate, localTime)
        .toInstant(sourceTimeZone)
        .toEpochMilliseconds()
}
