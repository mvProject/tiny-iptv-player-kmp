/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.07.24, 17:57
 *
 */

package com.mvproject.tinyiptvkmp.core.database.entity

import androidx.room.Entity

@Entity(tableName = "favoriteChannels", primaryKeys = ["channelUrl", "parentListId"])
data class FavoriteChannelEntity(
    val channelName: String,
    val channelUrl: String,
    val channelOrder: Long,
    val favoriteType: String,
    val parentListId: String,
)
