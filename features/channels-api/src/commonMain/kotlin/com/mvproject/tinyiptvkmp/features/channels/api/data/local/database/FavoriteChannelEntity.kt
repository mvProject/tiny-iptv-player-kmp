package com.mvproject.tinyiptvkmp.features.channels.api.data.local.database

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "favoriteChannels",
    primaryKeys = ["channelUrl", "parentListId"],
    indices = [
        Index(value = ["parentListId"]),
    ],
)
data class FavoriteChannelEntity(
    val channelName: String,
    val channelUrl: String,
    val channelOrder: Long,
    val favoriteType: String,
    val parentListId: String,
)
