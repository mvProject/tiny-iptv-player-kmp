/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.07.24, 18:08
 *
 */

package com.mvproject.tinyiptvkmp.core.domain.model

import androidx.compose.runtime.Immutable
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.domain.enums.FavoriteType

@Immutable
data class TvChannel(
    val channelName: String = String.empty,
    val channelUrl: String = String.empty,
    val channelLogo: String = String.empty,
    val programId: String = String.empty,
    val favoriteType: FavoriteType = FavoriteType.NONE,
    val programs: List<EpgProgram> = emptyList(),
) {
    override fun toString() = buildString {
        append("channelName: $channelName")
        append("\n")
        append("channelUrl: $channelUrl")
        append("\n")
        append("channelLogo: $channelLogo")
        append("\n")
        append("channelEpgCount: ${programs.count()}")
    }
}
