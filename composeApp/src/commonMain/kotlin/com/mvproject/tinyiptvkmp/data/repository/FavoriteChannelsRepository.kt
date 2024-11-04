/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 20:26
 *
 */

package com.mvproject.tinyiptvkmp.data.repository

import com.mvproject.tinyiptvkmp.data.enums.FavoriteType
import com.mvproject.tinyiptvkmp.data.model.channels.PlaylistChannel
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.database.AppDatabase
import com.mvproject.tinyiptvkmp.database.entity.FavoriteChannelEntity
import com.mvproject.tinyiptvkmp.utils.AppConstants.INT_VALUE_1
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteChannelsRepository(
    private val appDatabase: AppDatabase,
) {
    private val favoriteChannelDao = appDatabase.favoriteChannelDao()
    private val playlistDao = appDatabase.playlistDao()

    suspend fun addChannelToFavorite(
        channel: TvPlaylistChannel,
        favoriteType: FavoriteType,
    ) {
        withContext(Dispatchers.IO) {
            val favoriteCount = favoriteChannelDao.getFavoriteChannelCount()
            val playlistId = playlistDao.getSelectedPlaylistId()

            val order = (favoriteCount + INT_VALUE_1).toLong()

            favoriteChannelDao.insertFavoriteChannel(
                FavoriteChannelEntity(
                    channelName = channel.channelName,
                    channelUrl = channel.channelUrl,
                    channelOrder = order,
                    favoriteType = favoriteType,
                    parentListId = playlistId,
                ),
            )
        }
    }

    suspend fun updatePlaylistFavoriteChannels(channel: PlaylistChannel) {
        withContext(Dispatchers.IO) {
            favoriteChannelDao.updateFavoriteChannels(
                channelName = channel.channelName,
                channelUrl = channel.channelUrl,
            )
        }
    }

    suspend fun deleteChannelFromFavorite(channelUrl: String) {
        favoriteChannelDao.deleteChannelFromFavorite(channelUrl = channelUrl)
    }

    suspend fun loadSelectedFavoriteChannels(): List<FavTypes> =
        favoriteChannelDao.getSelectedFavoriteChannels().map { item ->
            FavTypes(
                url = item.channelUrl,
                type = item.favoriteType,
            )
        }

    suspend fun loadFavoriteChannelById(id: Long): List<FavTypes> =
        favoriteChannelDao.getFavoriteChannelById(id = id).map { item ->
            FavTypes(
                url = item.channelUrl,
                type = item.favoriteType,
            )
        }

    suspend fun loadFavoriteChannelUrls(): List<String> = favoriteChannelDao.getFavoriteChannelUrls()

    suspend fun deletePlaylistFavoriteChannels(listId: Long) {
        favoriteChannelDao.deletePlaylistFavoriteChannelEntities(id = listId)
    }
}

data class FavTypes(
    val url: String,
    val type: FavoriteType,
)
