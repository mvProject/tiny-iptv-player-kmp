/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.07.24, 18:08
 *
 */

package com.mvproject.tinyiptvkmp.data.model.channels

import androidx.compose.runtime.Immutable
import com.mvproject.tinyiptvkmp.data.enums.FavoriteType
import com.mvproject.tinyiptvkmp.data.model.epg.EpgProgram
import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty

@Immutable
data class TvPlaylistChannel(
    val channelName: String = String.empty,
    val channelUrl: String = String.empty,
    val channelLogo: String = String.empty,
    val epgId: String = String.empty,
    val favoriteType: FavoriteType = FavoriteType.NONE,
    val isEpgUsing: Boolean = false,
    val programs: List<EpgProgram> = emptyList(),
) {
    override fun toString() =
        StringBuilder()
            .append("channelName: $channelName")
            .append("\n")
            .append("channelUrl: $channelUrl")
            .append("\n")
            .append("channelLogo: $channelLogo")
            .append("\n")
            .append("channelEpgCount: ${programs.count()}")
            .toString()
}
