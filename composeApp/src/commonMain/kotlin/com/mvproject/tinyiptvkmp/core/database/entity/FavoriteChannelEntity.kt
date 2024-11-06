/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.07.24, 17:57
 *
 */

package com.mvproject.tinyiptvkmp.core.database.entity

import androidx.room.Entity
import com.mvproject.tinyiptvkmp.data.enums.FavoriteType

@Entity(tableName = "favoriteChannels", primaryKeys = ["channelName", "parentListId"])
data class FavoriteChannelEntity(
    val channelName: String,
    val channelUrl: String,
    val channelOrder: Long,
    val favoriteType: FavoriteType,
    val parentListId: Long,
)
