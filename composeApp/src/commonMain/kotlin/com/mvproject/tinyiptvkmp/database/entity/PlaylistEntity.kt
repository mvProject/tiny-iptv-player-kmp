/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:06
 *
 */

package com.mvproject.tinyiptvkmp.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mvproject.tinyiptvkmp.data.enums.PlaylistType

@Entity
data class PlaylistEntity(
    @PrimaryKey
    val id: Long,
    val playlistName: String,
    val playlistSource: String,
    val playlistType: PlaylistType,
    val lastUpdateDate: Long,
    val updatePeriod: Long,
)
