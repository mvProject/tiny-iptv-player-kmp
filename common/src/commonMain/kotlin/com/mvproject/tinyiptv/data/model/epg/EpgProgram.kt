/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 26.10.23, 15:44
 *
 */

package com.mvproject.tinyiptv.data.model.epg

import com.mvproject.tinyiptv.utils.AppConstants
import com.mvproject.tinyiptv.utils.TimeUtils.calculateProgramProgress

data class EpgProgram(
    val start: Long,
    val stop: Long,
    val channelId: String,
    val title: String = AppConstants.EMPTY_STRING,
    val description: String = AppConstants.EMPTY_STRING
) {

    val key
        get() = (start + stop).toString() + title

    val programProgress
        get() = calculateProgramProgress(
            startTime = start,
            endTime = stop
        )

    override fun toString() =
        StringBuilder()
            .append("\n")
            .append(channelId)
            .append("\n")
            .append("$start - $stop")
            .append("\n")
            .append("title: $title")
            .append("\n")
            .append("description: $description")
            .toString()
}