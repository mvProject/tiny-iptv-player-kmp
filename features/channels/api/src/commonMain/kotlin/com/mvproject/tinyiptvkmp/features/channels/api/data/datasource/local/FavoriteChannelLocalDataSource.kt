package com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.local

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteType

interface FavoriteChannelLocalDataSource {
    suspend fun addFavoriteChannel(
        channelName: String,
        channelUrl: String,
        channelOrder: Long,
        favoriteType: FavoriteType,
        playlistId: String,
    )

    suspend fun updateFavoriteChannel(
        playlistId: String,
        channelName: String,
        channelUrl: String,
    )

    suspend fun updateFavoriteChannels(
        playlistId: String,
        channelNamesByUrl: Map<String, String>,
    )

    suspend fun deleteFavoriteChannel(
        playlistId: String,
        channelUrl: String,
    )

    suspend fun updateFavoriteType(
        playlistId: String,
        channelUrl: String,
        favoriteType: FavoriteType,
    )

    suspend fun loadFavoriteChannelCount(playlistId: String): Int

    suspend fun loadSelectedFavoriteChannels(playlistId: String): List<FavoriteChannel>

    suspend fun loadFavoriteChannelsByPlaylistId(playlistId: String): List<FavoriteChannel>

    suspend fun loadFavoriteChannelUrls(): List<String>

    suspend fun loadFavoriteChannelUrls(playlistId: String): List<String>

    suspend fun deletePlaylistFavoriteChannels(playlistId: String)
}
