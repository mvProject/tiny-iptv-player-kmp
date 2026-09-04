package com.mvproject.tinyiptvkmp.persistence.channels.room.database

data class PlaylistChannelFavorite(
    val channelName: String,
    val channelUrl: String,
    val channelLogo: String,
    val programId: String,
    val favoriteType: String?,
)
