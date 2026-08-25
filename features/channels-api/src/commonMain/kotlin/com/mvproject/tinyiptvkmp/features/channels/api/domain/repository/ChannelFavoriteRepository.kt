package com.mvproject.tinyiptvkmp.features.channels.api.domain.repository

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteChannel

interface ChannelFavoriteRepository {
    suspend fun addChannelToFavorite(
        channelName: String,
        channelUrl: String,
        favoriteType: String,
    )

    suspend fun deleteChannelFromFavorite(channelUrl: String)

    suspend fun loadSelectedFavoriteChannels(): List<FavoriteChannel>

    suspend fun loadFavoriteChannelUrls(): List<String>

    suspend fun loadFavoriteChannelUrls(playlistId: String): List<String>

    suspend fun updateFavoriteChannel(
        channelName: String,
        channelUrl: String,
    )

    suspend fun deletePlaylistFavoriteChannels(playlistId: String)
}
