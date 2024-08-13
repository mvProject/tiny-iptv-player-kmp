/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 27.06.24, 14:40
 *
 */

package com.mvproject.tinyiptvkmp.data.model.epg

import com.mvproject.tinyiptvkmp.utils.AppConstants.LONG_VALUE_ZERO
import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty

data class EpgInfo(
    val channelId: String = String.empty,
    val channelName: String = String.empty,
    val channelLogo: String = String.empty,
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
