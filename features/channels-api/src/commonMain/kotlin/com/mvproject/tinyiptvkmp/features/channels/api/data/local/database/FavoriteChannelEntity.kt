package com.mvproject.tinyiptvkmp.features.channels.api.data.local.database

import androidx.room.Entity

@Entity(tableName = "favoriteChannels", primaryKeys = ["channelUrl", "parentListId"])
data class FavoriteChannelEntity(
    val channelName: String,
    val channelUrl: String,
    val channelOrder: Long,
    val favoriteType: String,
    val parentListId: String,
)
