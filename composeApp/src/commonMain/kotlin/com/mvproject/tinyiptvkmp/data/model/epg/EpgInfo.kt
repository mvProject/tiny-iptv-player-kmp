/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 27.06.24, 14:40
 *
 */

package com.mvproject.tinyiptvkmp.data.model.epg

import com.mvproject.tinyiptvkmp.utils.AppConstants
import com.mvproject.tinyiptvkmp.utils.AppConstants.LONG_VALUE_ZERO

data class EpgInfo(
    val channelId: String = AppConstants.EMPTY_STRING,
    val channelName: String = AppConstants.EMPTY_STRING,
    val channelLogo: String = AppConstants.EMPTY_STRING,
    val lastUpdate: Long = LONG_VALUE_ZERO,
) {
    override fun toString() =
        StringBuilder()
            .append("\n")
            .append("channelId: $channelId")
            .append("\n")
            .append("channelName: $channelName")
            .append("\n")
            .append("channelLogo: $channelLogo")
            .append("\n")
            .append("lastUpdate: $lastUpdate")
            .toString()
}
