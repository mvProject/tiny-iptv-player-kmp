/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.features.epg.api.domain.model

import kotlin.time.Clock

data class EpgProgram(
    val programId: String,
    val channelId: String = "",
    val title: String = "",
    val description: String = "",
    val dateTimeStart: Long,
    val dateTimeEnd: Long,
) {

    val programProgress
        get(): Float {
            val currentTime = Clock.System.now().toEpochMilliseconds()
            val duration = dateTimeEnd - dateTimeStart
            if (currentTime <= dateTimeStart || duration <= 0L) return 0f

            return (currentTime - dateTimeStart)
                .toDouble()
                .div(duration)
                .toFloat()
        }

    override fun toString() = buildString {
        append("\n")
        append(channelId)
        append("\n")
        append("$dateTimeStart - $dateTimeEnd")
        append("\n")
        append("title: $title")
        append("\n")
        append("description: $description")
    }
}
