/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:06
 *
 */

package com.mvproject.tinyiptvkmp.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty

@Entity(tableName = "epgPrograms")
data class EpgProgramEntity(
    @PrimaryKey
    val programId: String,
    val channelId: String = String.empty,
    val title: String = String.empty,
    val description: String = String.empty,
    val dateTimeStart: Long,
    val dateTimeEnd: Long,
)
