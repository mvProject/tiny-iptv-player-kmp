/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 06.05.24, 11:33
 *
 */

package com.mvproject.tinyiptvkmp.data.model.channels

import com.mvproject.tinyiptvkmp.ui.screens.channels.data.TvPlaylistChannelEpg
import com.mvproject.tinyiptvkmp.utils.AppConstants.EMPTY_STRING

data class TvPlaylistChannel(
    val channelName: String = EMPTY_STRING,
    val channelUrl: String = EMPTY_STRING,
    val channelLogo: String = EMPTY_STRING,
    val epgId: String = EMPTY_STRING,
    val isInFavorites: Boolean = false,
    val isEpgUsing: Boolean = false,
    val channelEpg: TvPlaylistChannelEpg = TvPlaylistChannelEpg(),
) {
    override fun toString() =
        StringBuilder()
            .append("channelName: $channelName")
            .append("\n")
            .append("channelUrl: $channelUrl")
            .append("\n")
            .append("channelLogo: $channelLogo")
            .append("\n")
            .append("channelEpgCount: ${channelEpg.items.count()}")
            .toString()
}
