/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 27.06.24, 14:40
 *
 */

package com.mvproject.tinyiptvkmp.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "epgChannels")
data class EpgChannelEntity(
    @PrimaryKey val id: String,
    val programId: String,
    val title: String,
    val logo: String,
)
