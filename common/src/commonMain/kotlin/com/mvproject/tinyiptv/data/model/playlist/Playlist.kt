/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 26.10.23, 15:44
 *
 */

package com.mvproject.tinyiptv.data.model.playlist

import com.mvproject.tinyiptv.utils.AppConstants
import com.mvproject.tinyiptv.utils.AppConstants.EMPTY_STRING
import kotlin.random.Random

data class Playlist(
    val id: Long = Random.nextLong(),
    val playlistTitle: String = EMPTY_STRING,
    val playlistUrl: String = EMPTY_STRING,
    val playlistLocalName: String = EMPTY_STRING,
    val lastUpdateDate: Long = AppConstants.LONG_VALUE_ZERO,
    val updatePeriod: Long = AppConstants.LONG_VALUE_ZERO,
    val isLocalSource: Boolean = false
) {
    override fun toString(): String {
        return StringBuilder()
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
}