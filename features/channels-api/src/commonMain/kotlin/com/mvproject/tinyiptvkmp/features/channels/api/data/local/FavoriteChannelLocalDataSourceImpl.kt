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
        channelName: String,
        channelUrl: String,
    ) {
        favoriteChannelDao.updateFavoriteChannels(
            channelName = channelName,
            channelUrl = channelUrl,
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

    override suspend fun deletePlaylistFavoriteChannels(playlistId: String) {
        favoriteChannelDao.deletePlaylistFavoriteChannelEntities(playlistId = playlistId)
    }
}
