/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:06
 *
 */

package com.mvproject.tinyiptvkmp.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey
    val id: String,
    val playlistName: String,
    val playlistSource: String,
    val playlistType: String,
    val lastUpdateDate: Long,
    val updatePeriod: Long,
    val isSelected: Boolean,
)
