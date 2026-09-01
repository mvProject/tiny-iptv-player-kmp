/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.features.playlist.api.domain.model

data class Playlist(
    val id: String = "",
    val playlistName: String = "",
    val playlistSource: String = "",
    val playlistType: PlaylistType = PlaylistType.REMOTE,
    val lastUpdateDate: Long = 0L,
    val updatePeriod: Long = 0L,
    val isSelected: Boolean = false,
) {
    override fun toString(): String = buildString {
        append("\n")
        append("playlistName - $playlistName")
        append("\n")
        append("playlistSource - $playlistSource")
        append("\n")
        append("playlistType - $playlistType")
        append("\n")
        append("updatePeriod - $updatePeriod")
        append("\n")
        append("lastUpdateDate - $lastUpdateDate")
        append("\n")
        append("isSelected - $isSelected")
    }
}
