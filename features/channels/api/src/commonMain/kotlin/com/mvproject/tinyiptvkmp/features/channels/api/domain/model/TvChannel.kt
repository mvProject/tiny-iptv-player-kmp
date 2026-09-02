/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.07.24, 18:08
 *
 */

package com.mvproject.tinyiptvkmp.features.channels.api.domain.model

data class TvChannel(
    val channelName: String = "",
    val channelUrl: String = "",
    val channelLogo: String = "",
    val programId: String = "",
    val favoriteType: FavoriteType = FavoriteType.NONE,
) {
    override fun toString() = buildString {
        append("channelName: $channelName")
        append("\n")
        append("channelUrl: $channelUrl")
        append("\n")
        append("channelLogo: $channelLogo")
        append("\n")
        append("programId: $programId")
    }
}
