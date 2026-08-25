/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 09.05.24, 20:16
 *
 */

package com.mvproject.tinyiptvkmp.features.channels.api.data.local.database

import androidx.room.Entity

@Entity(tableName = "playlistChannels", primaryKeys = ["channelUrl", "parentListId"])
data class PlaylistChannelEntity(
    val channelUrl: String,
    val channelName: String,
    val channelLogo: String,
    val channelGroup: String,
    val programId: String,
    val parentListId: String,
)
