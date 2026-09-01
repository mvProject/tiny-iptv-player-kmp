package com.mvproject.tinyiptvkmp.persistence.channels.room

import com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.local.FavoriteChannelLocalDataSource
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteChannel
import com.mvproject.tinyiptvkmp.persistence.channels.room.database.FavoriteChannelDao
import com.mvproject.tinyiptvkmp.persistence.channels.room.mapper.favoriteChannelEntity
import com.mvproject.tinyiptvkmp.persistence.channels.room.mapper.toFavoriteChannel

internal class RoomFavoriteChannelLocalDataSource(
    private val favoriteChannelDao: FavoriteChannelDao,
) : FavoriteChannelLocalDataSource {
    override suspend fun addFavoriteChannel(
        channelName: String,
        channelUrl: String,
        channelOrder: Long,
        favoriteType: String,
        playlistId: String,
    ) {
        favoriteChannelDao.insertFavoriteChannel(
            data = favoriteChannelEntity(
                channelName = channelName,
                channelUrl = channelUrl,
                channelOrder = channelOrder,
                playlistId = playlistId,
                favoriteType = favoriteType
            )
        )
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

    override suspend fun loadSelectedFavoriteChannels(playlistId: String): List<FavoriteChannel> =
        favoriteChannelDao.getSelectedFavoriteChannels(playlistId = playlistId)
            .map { it.toFavoriteChannel() }

    override suspend fun loadFavoriteChannelsByPlaylistId(playlistId: String): List<FavoriteChannel> =
        favoriteChannelDao.getFavoriteChannelsByPlaylistId(playlistId = playlistId)
            .map { it.toFavoriteChannel() }

    override suspend fun loadFavoriteChannelUrls(): List<String> =
        favoriteChannelDao.getFavoriteChannelUrls()

    override suspend fun loadFavoriteChannelUrls(playlistId: String): List<String> =
        favoriteChannelDao.getFavoriteChannelUrls(playlistId = playlistId)

    override suspend fun deletePlaylistFavoriteChannels(playlistId: String) {
        favoriteChannelDao.deletePlaylistFavoriteChannelEntities(playlistId = playlistId)
    }
}