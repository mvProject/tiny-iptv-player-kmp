package com.mvproject.tinyiptvkmp.features.channels.api.data.repository

import com.mvproject.tinyiptvkmp.features.channels.api.data.local.FavoriteChannelLocalDataSource
import com.mvproject.tinyiptvkmp.features.channels.api.data.mapper.favoriteChannelEntity
import com.mvproject.tinyiptvkmp.features.channels.api.data.mapper.toFavoriteChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelFavoriteRepository
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.SelectedPlaylistProvider

internal class ChannelFavoriteRepositoryImpl(
    private val local: FavoriteChannelLocalDataSource,
    private val selectedPlaylistProvider: SelectedPlaylistProvider,
) : ChannelFavoriteRepository {
    override suspend fun addChannelToFavorite(
        channelName: String,
        channelUrl: String,
        favoriteType: String,
    ) {
        val playlistId = selectedPlaylistProvider.getSelectedPlaylistId()
        val order = local.loadFavoriteChannelCount(playlistId = playlistId) + 1L

        local.addFavoriteChannel(
            favoriteChannelEntity(
                channelName = channelName,
                channelUrl = channelUrl,
                channelOrder = order,
                favoriteType = favoriteType,
                playlistId = playlistId,
            ),
        )
    }

    override suspend fun deleteChannelFromFavorite(channelUrl: String) {
        local.deleteFavoriteChannel(
            playlistId = selectedPlaylistProvider.getSelectedPlaylistId(),
            channelUrl = channelUrl,
        )
    }

    override suspend fun loadSelectedFavoriteChannels(): List<FavoriteChannel> =
        local
            .loadSelectedFavoriteChannels(
                playlistId = selectedPlaylistProvider.getSelectedPlaylistId(),
            ).map { entity -> entity.toFavoriteChannel() }

    override suspend fun loadFavoriteChannelUrls(): List<String> =
        local.loadFavoriteChannelUrls()

    override suspend fun loadFavoriteChannelUrls(playlistId: String): List<String> =
        local.loadFavoriteChannelUrls(playlistId = playlistId)

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
