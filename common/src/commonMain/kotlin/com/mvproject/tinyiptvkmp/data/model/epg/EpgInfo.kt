/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.model.epg

import com.mvproject.tinyiptvkmp.utils.AppConstants

data class EpgInfo(
    val channelId: String = AppConstants.EMPTY_STRING,
    val channelName: String = AppConstants.EMPTY_STRING,
    val channelLogo: String = AppConstants.EMPTY_STRING,
) {
    override fun toString() =
        StringBuilder()
            .append("\n")
            .append("channelId: $channelId")
            .append("\n")
            .append("channelName: $channelName")
            .append("\n")
            .append("channelLogo: $channelLogo")
            .toString()
}