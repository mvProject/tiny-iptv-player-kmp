package com.mvproject.tinyiptvkmp.features.channels.api.data.repository

import com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.local.FavoriteChannelLocalDataSource
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelFavoriteRepository

internal class ChannelFavoriteRepositoryImpl(
    private val local: FavoriteChannelLocalDataSource,
) : ChannelFavoriteRepository {
    override suspend fun addChannelToFavorite(
        playlistId: String,
        channelName: String,
        channelUrl: String,
        favoriteType: FavoriteType,
    ) {
        val order = local.loadFavoriteChannelCount(playlistId = playlistId) + 1L

        local.addFavoriteChannel(
            channelName = channelName,
            channelUrl = channelUrl,
            channelOrder = order,
            favoriteType = favoriteType,
            playlistId = playlistId,
        )
    }

    override suspend fun deleteChannelFromFavorite(playlistId: String, channelUrl: String) {
        local.deleteFavoriteChannel(
            playlistId = playlistId,
            channelUrl = channelUrl,
        )
    }

    override suspend fun updateFavoriteType(
        playlistId: String,
        channelUrl: String,
        favoriteType: FavoriteType,
    ) {
        local.updateFavoriteType(
            playlistId = playlistId,
            channelUrl = channelUrl,
            favoriteType = favoriteType,
        )
    }

    override suspend fun loadSelectedFavoriteChannels(playlistId: String): List<FavoriteChannel> =
        local.loadSelectedFavoriteChannels(playlistId = playlistId)

    override suspend fun loadFavoriteChannelUrls(): List<String> =
        local.loadFavoriteChannelUrls()

    override suspend fun loadFavoriteChannelUrls(playlistId: String): List<String> =
        local.loadFavoriteChannelUrls(playlistId = playlistId)

    override suspend fun loadFavoriteChannelNamesByUrl(playlistId: String): Map<String, String> =
        local.loadFavoriteChannelNamesByUrl(playlistId = playlistId)

    override suspend fun updateFavoriteChannel(
        playlistId: String,
        channelName: String,
        channelUrl: String,
    ) {
        local.updateFavoriteChannel(
            playlistId = playlistId,
            channelName = channelName,
            channelUrl = channelUrl,
        )
    }

    override suspend fun updateFavoriteChannels(
        playlistId: String,
        channelNamesByUrl: Map<String, String>,
    ) {
        if (channelNamesByUrl.isEmpty()) return

        local.updateFavoriteChannels(
            playlistId = playlistId,
            channelNamesByUrl = channelNamesByUrl,
        )
    }

    override suspend fun deletePlaylistFavoriteChannels(playlistId: String) {
        local.deletePlaylistFavoriteChannels(playlistId = playlistId)
    }
}
