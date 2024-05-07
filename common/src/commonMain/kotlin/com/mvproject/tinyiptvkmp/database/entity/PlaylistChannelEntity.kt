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
data class PlaylistChannelEntity(
    @PrimaryKey
    val channelUrl: String,
    val channelName: String,
    val channelLogo: String,
    val channelGroup: String,
    val epgId: String,
    val parentListId: Long,
)
