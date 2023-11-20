/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:28
 *
 */

package com.mvproject.tinyiptv.data.model.epg

import com.mvproject.tinyiptv.utils.AppConstants

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