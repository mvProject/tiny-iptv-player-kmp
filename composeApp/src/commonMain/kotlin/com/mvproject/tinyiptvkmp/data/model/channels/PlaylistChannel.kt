/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.model.channels

import com.mvproject.tinyiptvkmp.utils.AppConstants.EMPTY_STRING

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