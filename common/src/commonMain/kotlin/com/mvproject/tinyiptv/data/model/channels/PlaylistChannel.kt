/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.data.model.channels

import com.mvproject.tinyiptv.utils.AppConstants.EMPTY_STRING

data class PlaylistChannel(
    val channelName: String,
    val channelLogo: String = EMPTY_STRING,
    val channelUrl: String,
    val channelGroup: String,
    val epgId: String = EMPTY_STRING,
    val parentListId: Long
) {
    override fun toString() =
        StringBuilder()
            .append("\n")
            .append("channelName: $channelName")
            .append("\n")
            .append("channelLogo: $channelLogo")
            .append("\n")
            .append("channelUrl: $channelUrl")
            .append("\n")
            .append("channelGroup: $channelGroup")
            .append("\n")
            .append("epgId: $epgId")
            .append("\n")
            .append("parentListId: $parentListId")
            .toString()
}