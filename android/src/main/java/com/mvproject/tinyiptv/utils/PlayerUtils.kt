/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 05.05.23, 17:07
 *
 */

package com.mvproject.tinyiptv.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BrightnessHigh
import androidx.compose.material.icons.rounded.BrightnessLow
import androidx.compose.material.icons.rounded.BrightnessMedium
import androidx.compose.material.icons.rounded.VolumeDown
import androidx.compose.material.icons.rounded.VolumeMute
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.ui.unit.Constraints
import androidx.media3.common.PlaybackException
import androidx.media3.common.VideoSize
import com.mvproject.tinyiptv.data.enums.ResizeMode
import com.mvproject.tinyiptv.data.enums.UpdatePeriod
import com.mvproject.tinyiptv.utils.AppConstants.EMPTY_STRING
import io.github.aakira.napier.Napier
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toInstant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

private const val MAX_ASPECT_RATIO_DIFFERENCE_FRACTION = 0.01f

internal fun VideoSize.aspectRatio(): Float =
    if (height == 0 || width == 0) 0f else (width * pixelWidthHeightRatio) / height

internal fun Constraints.resizeForVideo(
    mode: ResizeMode,
    aspectRatio: Float
): Constraints {
    if (aspectRatio <= 0f) return this

    var width = maxWidth
    var height = maxHeight
    val constraintAspectRatio: Float = (width / height).toFloat()
    val difference = aspectRatio / constraintAspectRatio - 1

    if (kotlin.math.abs(difference) <= MAX_ASPECT_RATIO_DIFFERENCE_FRACTION) {
        // TODO: ?
        return this
    }

    when (mode) {
        ResizeMode.Fit -> {
            if (difference > 0) {
                height = (width / aspectRatio).toInt()
            } else {
                width = (height * aspectRatio).toInt()
            }
        }

        ResizeMode.Zoom -> {
            if (difference > 0) {
                width = (height * aspectRatio).toInt()
            } else {
                height = (width / aspectRatio).toInt()
            }
        }

        ResizeMode.FixedWidth -> {
            height = (width / aspectRatio).toInt()
        }

        ResizeMode.FixedHeight -> {
            width = (height * aspectRatio).toInt()
        }

        ResizeMode.Fill -> Unit
    }

    return this.copy(maxWidth = width, maxHeight = height)
}

/**
 * Will return a timestamp denoting the current video [position] and the [duration] in the following
 * format "mm:ss / mm:ss"
 * **/
internal fun prettyVideoTimestamp(
    duration: Duration,
    position: Duration
): String = buildString {
    appendMinutesAndSeconds(duration)
    append(" / ")
    appendMinutesAndSeconds(position)
}

/**
 * Will split [duration] in minutes and seconds and append it to [this] in the following format "mm:ss"
 * */
private fun StringBuilder.appendMinutesAndSeconds(duration: Duration) {
    val minutes = duration.inWholeMinutes
    val seconds = (duration - minutes.minutes).inWholeSeconds
    appendDoubleDigit(minutes)
    append(':')
    appendDoubleDigit(seconds)
}

/**
 * Will append [value] as double digit to [this].
 * If a single digit value is passed, ex: 4 then a 0 will be added as prefix resulting in 04
 * */
private fun StringBuilder.appendDoubleDigit(value: Long) {
    if (value < 10) {
        append(0)
        append(value)
    } else {
        append(value)
    }
}


fun Context.findActivity(): Activity {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    throw IllegalStateException("no activity")
}

fun String.dateEpgToIsoString(): String {
    val dateString = this.split("+").first().trim()

    val year = dateString.take(4)
    val date = dateString.drop(4).take(4).chunked(2).joinToString("-")
    val endTime = dateString.takeLast(6).chunked(2).joinToString(":")
    return StringBuilder()
        .append(year)
        .append("-")
        .append(date)
        .append("T")
        .append(endTime)
        .toString()
}

fun String.isoStringToMillis(): Long {
    val localDateTime = LocalDateTime.parse(this)
    return localDateTime
        //.toInstant(TimeZone.currentSystemDefault())
        //.toEpochMilliseconds()

        .toInstant(TimeUtils.timeZoneSource)
        .toEpochMilliseconds()
}

fun getProperVolumeIcon(value: Int) = when {
    value < 35 -> Icons.Rounded.VolumeMute
    value > 65 -> Icons.Rounded.VolumeUp
    else -> Icons.Rounded.VolumeDown
}

fun getProperBrightnessIcon(value: Int) = when {
    value < 35 -> Icons.Rounded.BrightnessLow
    value > 65 -> Icons.Rounded.BrightnessHigh
    else -> Icons.Rounded.BrightnessMedium
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

fun typeToDuration(type: Int): Long =
    when (type) {
        UpdatePeriod.NO_UPDATE.value -> {
            AppConstants.LONG_VALUE_ZERO
        }

        UpdatePeriod.HOURS_6.value -> {
            6.hours.inWholeMilliseconds
        }

        UpdatePeriod.HOURS_12.value -> {
            12.hours.inWholeMilliseconds
        }

        UpdatePeriod.HOURS_24.value -> {
            24.hours.inWholeMilliseconds
        }

        UpdatePeriod.DAYS_2.value -> {
            2.days.inWholeMilliseconds
        }

        UpdatePeriod.WEEK_1.value -> {
            7.days.inWholeMilliseconds
        }

        else -> {
            AppConstants.LONG_NO_VALUE
        }
    }

fun isMediaPlayable(errorCode: Int?): Boolean {
    val isMediaPlayable = when (errorCode) {
        PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> false
        PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS -> false
        PlaybackException.ERROR_CODE_PARSING_MANIFEST_MALFORMED -> false
        else -> true
    }
    Napier.e("testing errorCode:$errorCode, isMediaPlayable:$isMediaPlayable")
    return isMediaPlayable
}

fun String.getNameFromStringUri() =
    Uri.parse(this).path?.split("/")?.last() ?: EMPTY_STRING
