/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.core.data.model

import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty

data class PlaylistChannel(
    val channelName: String,
    val channelLogo: String = String.empty,
    val channelUrl: String,
    val channelGroup: String,
    val epgId: String = String.empty,
    val parentListId: Long,
) {
    override fun toString() = buildString {
        append("\n")
        append("channelName: $channelName")
        append("\n")
        append("channelLogo: $channelLogo")
        append("\n")
        append("channelUrl: $channelUrl")
        append("\n")
        append("channelGroup: $channelGroup")
        append("\n")
        append("epgId: $epgId")
        append("\n")
        append("parentListId: $parentListId")
    }
}
