/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.data.model.channels

import com.mvproject.tinyiptv.data.model.epg.EpgProgram
import com.mvproject.tinyiptv.utils.AppConstants.EMPTY_STRING

data class TvPlaylistChannel(
    val channelName: String = EMPTY_STRING,
    val channelUrl: String = EMPTY_STRING,
    val channelLogo: String = EMPTY_STRING,
    val epgId: String = EMPTY_STRING,
    val isInFavorites: Boolean = false,
    val isEpgUsing: Boolean = false,
    val channelEpg: List<EpgProgram> = emptyList()
) {
    override fun toString() =
        StringBuilder()
            .append("channelName: $channelName")
            .append("\n")
            .append("channelUrl: $channelUrl")
            .append("\n")
            .append("channelLogo: $channelLogo")
            .append("\n")
            .append("channelEpgCount: ${channelEpg.count()}")
            .toString()
}