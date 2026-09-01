/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.features.channels.api.domain.model

data class PlaylistChannel(
    val channelName: String,
    val channelLogo: String = "",
    val channelUrl: String,
    val channelGroup: String,
    val programId: String = "",
    val parentListId: String,
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
        append("programId: $programId")
        append("\n")
        append("parentListId: $parentListId")
    }
}
