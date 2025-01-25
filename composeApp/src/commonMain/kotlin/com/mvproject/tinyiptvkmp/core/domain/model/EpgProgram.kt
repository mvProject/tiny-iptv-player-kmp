/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.core.domain.model

import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.common.utils.calculateProgramProgress

data class EpgProgram(
    val programId: String,
    val channelId: String = String.empty,
    val title: String = String.empty,
    val description: String = String.empty,
    val dateTimeStart: Long,
    val dateTimeEnd: Long,
) {

    val programProgress
        get() =
            calculateProgramProgress(
                startTime = dateTimeStart,
                endTime = dateTimeEnd,
            )

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
