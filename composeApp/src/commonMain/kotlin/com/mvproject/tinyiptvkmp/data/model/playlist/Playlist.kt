/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.model.playlist

import com.mvproject.tinyiptvkmp.utils.AppConstants.LONG_VALUE_ZERO
import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty
import kotlin.random.Random

data class Playlist(
    val id: Long = Random.nextLong(),
    val playlistTitle: String = String.empty,
    val playlistUrl: String = String.empty,
    val playlistLocalName: String = String.empty,
    val lastUpdateDate: Long = LONG_VALUE_ZERO,
    val updatePeriod: Long = LONG_VALUE_ZERO,
    val isLocalSource: Boolean = false,
) {
    override fun toString(): String =
        StringBuilder()
            .append("\n")
            .append("name - $playlistTitle")
            .append("\n")
            .append("listUrl - $playlistUrl")
            .append("\n")
            .append("listUrl - $playlistLocalName")
            .append("\n")
            .append("isLocalSource - $isLocalSource")
            .append("\n")
            .append("updatePeriod - $updatePeriod")
            .append("\n")
            .append("lastUpdateDate - $lastUpdateDate")
            .toString()
}
