/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 20:26
 *
 */

package com.mvproject.tinyiptvkmp.core.data.repository

import com.mvproject.tinyiptvkmp.core.common.INT_VALUE_1
import com.mvproject.tinyiptvkmp.core.database.db.AppDatabase
import com.mvproject.tinyiptvkmp.core.database.entity.FavoriteChannelEntity
import com.mvproject.tinyiptvkmp.core.domain.enums.FavoriteType
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

class FavoriteChannelsRepository(
    private val appDatabase: AppDatabase,
) : KoinComponent {
    private val logger by injectLogger()

    private val favoriteChannelDao = appDatabase.favoriteChannelDao()
    private val playlistDao = appDatabase.playlistDao()

    suspend fun addChannelToFavorite(
        channelName: String,
        channelUrl: String,
        favoriteType: FavoriteType,
    ) {
        withContext(Dispatchers.IO) {
            val favoriteCount = favoriteChannelDao.getFavoriteChannelCount()
            val playlistId = playlistDao.getSelectedPlaylistId()
            logger.e { "testing FavoriteChannelsRepository addChannelToFavorite favoriteCount:$favoriteCount" }
            logger.e { "testing FavoriteChannelsRepository addChannelToFavorite favoriteCount:$playlistId" }
            val order = (favoriteCount + INT_VALUE_1).toLong()

            favoriteChannelDao.insertFavoriteChannel(
                FavoriteChannelEntity(
                    channelName = channelName,
                    channelUrl = channelUrl,
                    channelOrder = order,
                    favoriteType = favoriteType.name,
                    parentListId = playlistId,
                )
            )
        }
    }

    suspend fun updatePlaylistFavoriteChannels(
        channelName: String,
        channelUrl: String,
    ) {
        withContext(Dispatchers.IO) {
            favoriteChannelDao.updateFavoriteChannels(
                channelName = channelName,
                channelUrl = channelUrl,
            )
        }
    }

    suspend fun deleteChannelFromFavorite(channelUrl: String) =
        favoriteChannelDao.deleteChannelFromFavorite(channelUrl = channelUrl)

    suspend fun loadSelectedFavoriteChannels(): List<FavoriteChannelEntity> =
        favoriteChannelDao.getSelectedFavoriteChannels()

    suspend fun loadFavoriteChannelById(id: String): List<FavoriteChannelEntity> =
        favoriteChannelDao.getFavoriteChannelById(id = id)

    suspend fun loadFavoriteChannelUrls(): List<String> =
        favoriteChannelDao.getFavoriteChannelUrls()

    suspend fun deletePlaylistFavoriteChannels(listId: String) {
        favoriteChannelDao.deletePlaylistFavoriteChannelEntities(id = listId)
    }
}
