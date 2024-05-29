/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:06
 *
 */

package com.mvproject.tinyiptvkmp.database.entity

import androidx.room.Entity

@Entity(primaryKeys = ["programStart", "programEnd", "title"])
data class EpgProgramEntity(
    val channelId: String,
    val programStart: Long,
    val programEnd: Long,
    val title: String,
    val description: String,
)
