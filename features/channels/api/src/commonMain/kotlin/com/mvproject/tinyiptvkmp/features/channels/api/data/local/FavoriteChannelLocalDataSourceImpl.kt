package com.mvproject.tinyiptvkmp.features.channels.api.data.local

import com.mvproject.tinyiptvkmp.features.channels.api.data.local.database.FavoriteChannelDao
import com.mvproject.tinyiptvkmp.features.channels.api.data.local.database.FavoriteChannelEntity

internal class FavoriteChannelLocalDataSourceImpl(
    private val favoriteChannelDao: FavoriteChannelDao,
) : FavoriteChannelLocalDataSource {
    override suspend fun addFavoriteChannel(channel: FavoriteChannelEntity) {
        favoriteChannelDao.insertFavoriteChannel(data = channel)
    }

    override suspend fun updateFavoriteChannel(
        playlistId: String,
        channelName: String,
        channelUrl: String,
    ) {
        favoriteChannelDao.updateFavoriteChannel(
            playlistId = playlistId,
            channelName = channelName,
            channelUrl = channelUrl,
        )
    }

    override suspend fun updateFavoriteChannels(
        playlistId: String,
        channelNamesByUrl: Map<String, String>,
    ) {
        favoriteChannelDao.updateFavoriteChannels(
            playlistId = playlistId,
            channelNamesByUrl = channelNamesByUrl,
        )
    }

    override suspend fun deleteFavoriteChannel(
        playlistId: String,
        channelUrl: String,
    ) {
        favoriteChannelDao.deleteChannelFromFavorite(
            playlistId = playlistId,
            channelUrl = channelUrl,
        )
    }

    override suspend fun loadFavoriteChannelCount(playlistId: String): Int =
        favoriteChannelDao.getFavoriteChannelCount(playlistId = playlistId)

    override suspend fun loadSelectedFavoriteChannels(playlistId: String): List<FavoriteChannelEntity> =
        favoriteChannelDao.getSelectedFavoriteChannels(playlistId = playlistId)

    override suspend fun loadFavoriteChannelsByPlaylistId(playlistId: String): List<FavoriteChannelEntity> =
        favoriteChannelDao.getFavoriteChannelsByPlaylistId(playlistId = playlistId)

    override suspend fun loadFavoriteChannelUrls(): List<String> =
        favoriteChannelDao.getFavoriteChannelUrls()

    override suspend fun loadFavoriteChannelUrls(playlistId: String): List<String> =
        favoriteChannelDao.getFavoriteChannelUrls(playlistId = playlistId)

    override suspend fun deletePlaylistFavoriteChannels(playlistId: String) {
        favoriteChannelDao.deletePlaylistFavoriteChannelEntities(playlistId = playlistId)
    }
}
