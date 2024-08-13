/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.07.24, 18:08
 *
 */

package com.mvproject.tinyiptvkmp.data.model.channels

import com.mvproject.tinyiptvkmp.data.enums.FavoriteType
import com.mvproject.tinyiptvkmp.ui.screens.channels.data.TvPlaylistChannelEpg
import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty

data class TvPlaylistChannel(
    val channelName: String = String.empty,
    val channelUrl: String = String.empty,
    val channelLogo: String = String.empty,
    val epgId: String = String.empty,
    val favoriteType: FavoriteType = FavoriteType.NONE,
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
