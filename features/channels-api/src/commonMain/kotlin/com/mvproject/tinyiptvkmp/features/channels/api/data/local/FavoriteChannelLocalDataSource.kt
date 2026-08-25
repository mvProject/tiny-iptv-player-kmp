package com.mvproject.tinyiptvkmp.features.channels.api.data.local

import com.mvproject.tinyiptvkmp.features.channels.api.data.local.database.FavoriteChannelEntity

internal interface FavoriteChannelLocalDataSource {
    suspend fun addFavoriteChannel(channel: FavoriteChannelEntity)

    suspend fun updateFavoriteChannel(
        channelName: String,
        channelUrl: String,
    )

    suspend fun deleteFavoriteChannel(
        playlistId: String,
        channelUrl: String,
    )

    suspend fun loadFavoriteChannelCount(playlistId: String): Int

    suspend fun loadSelectedFavoriteChannels(playlistId: String): List<FavoriteChannelEntity>

    suspend fun loadFavoriteChannelsByPlaylistId(playlistId: String): List<FavoriteChannelEntity>

    suspend fun loadFavoriteChannelUrls(): List<String>

    suspend fun deletePlaylistFavoriteChannels(playlistId: String)
}
