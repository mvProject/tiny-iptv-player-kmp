/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:06
 *
 */

package com.mvproject.tinyiptvkmp.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlaylistEntity(
    @PrimaryKey
    val id: Long,
    val title: String,
    val localFilename: String,
    val url: String,
    val lastUpdateDate: Long,
    val updatePeriod: Long,
    val isLocal: Long,
)
