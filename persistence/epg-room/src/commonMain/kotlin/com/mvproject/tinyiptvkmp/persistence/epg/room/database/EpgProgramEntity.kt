/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:06
 *
 */

package com.mvproject.tinyiptvkmp.persistence.epg.room.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "epgPrograms")
data class EpgProgramEntity(
    @PrimaryKey
    val programId: String,
    val channelId: String = "",
    val title: String = "",
    val description: String = "",
    val dateTimeStart: Long,
    val dateTimeEnd: Long,
)
