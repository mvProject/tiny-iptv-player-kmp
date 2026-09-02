package com.mvproject.tinyiptvkmp.persistence.channels.room.database

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "favoriteChannels",
    primaryKeys = ["channelUrl", "parentListId"],
    indices = [
        Index(value = ["parentListId"]),
        Index(value = ["parentListId", "favoriteType"]),
    ],
)
data class FavoriteChannelEntity(
    val channelName: String,
    val channelUrl: String,
    val channelOrder: Long,
    val favoriteType: String,
    val parentListId: String,
)
