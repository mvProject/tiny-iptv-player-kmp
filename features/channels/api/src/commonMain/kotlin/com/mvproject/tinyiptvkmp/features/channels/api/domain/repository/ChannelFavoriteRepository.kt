package com.mvproject.tinyiptvkmp.features.channels.api.domain.repository

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteChannel

interface ChannelFavoriteRepository {
    suspend fun addChannelToFavorite(
        playlistId: String,
        channelName: String,
        channelUrl: String,
        favoriteType: String,
    )

    suspend fun deleteChannelFromFavorite(playlistId: String, channelUrl: String)

    suspend fun loadSelectedFavoriteChannels(playlistId: String): List<FavoriteChannel>

    suspend fun loadFavoriteChannelUrls(): List<String>

    suspend fun loadFavoriteChannelUrls(playlistId: String): List<String>

    suspend fun updateFavoriteChannel(
        playlistId: String,
        channelName: String,
        channelUrl: String,
    )

    suspend fun updateFavoriteChannels(
        playlistId: String,
        channelNamesByUrl: Map<String, String>,
    )

    suspend fun deletePlaylistFavoriteChannels(playlistId: String)
}
