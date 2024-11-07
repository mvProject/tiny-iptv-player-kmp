/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.core.domain.model

import com.mvproject.tinyiptvkmp.data.enums.PlaylistType
import com.mvproject.tinyiptvkmp.utils.AppConstants.LONG_VALUE_ZERO
import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty
import kotlin.random.Random

data class Playlist(
    val id: Long = Random.nextLong(),
    val playlistName: String = String.empty,
    val playlistSource: String = String.empty,
    val playlistType: PlaylistType = PlaylistType.REMOTE,
    val lastUpdateDate: Long = LONG_VALUE_ZERO,
    val updatePeriod: Long = LONG_VALUE_ZERO,
    val isSelected: Boolean = false,
) {
    override fun toString(): String =
        StringBuilder()
            .append("\n")
            .append("playlistName - $playlistName")
            .append("\n")
            .append("playlistSource - $playlistSource")
            .append("\n")
            .append("playlistType - $playlistType")
            .append("\n")
            .append("updatePeriod - $updatePeriod")
            .append("\n")
            .append("lastUpdateDate - $lastUpdateDate")
            .append("\n")
            .append("isSelected - $isSelected")
            .toString()
}
