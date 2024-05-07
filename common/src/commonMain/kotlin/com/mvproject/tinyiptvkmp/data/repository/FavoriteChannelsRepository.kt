/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:29
 *
 */

package com.mvproject.tinyiptvkmp.data.repository

import com.mvproject.tinyiptvkmp.data.model.channels.PlaylistChannel
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.database.AppDatabase
import com.mvproject.tinyiptvkmp.database.entity.FavoriteChannelEntity
import com.mvproject.tinyiptvkmp.utils.AppConstants.INT_VALUE_1
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteChannelsRepository(
    //  private val db: TinyIptvKmpDatabase,
    private val appDatabase: AppDatabase,
) {
    //  private val favoriteChannelQueries = db.favoriteChannelEntityQueries
    private val favoriteChannelDao = appDatabase.favoriteChannelDao()

    /*    suspend fun addChannelToFavorite(
            channel: TvPlaylistChannel,
            listId: Long,
        ) {
            withContext(Dispatchers.IO) {
                val favoriteCount =
                    favoriteChannelQueries.getPlaylistFavoriteChannelCount(id = listId)
                        .executeAsOne()
                        .toInt()

                val order = (favoriteCount + INT_VALUE_1).toLong()

                favoriteChannelQueries.addFavoriteChannelEntity(
                    FavoriteChannelEntity(
                        channelName = channel.channelName,
                        channelUrl = channel.channelUrl,
                        channelOrder = order,
                        parentListId = listId,
                    ),
                )
            }
        }*/

    suspend fun addChannelToFavorite(
        channel: TvPlaylistChannel,
        listId: Long,
    ) {
        withContext(Dispatchers.IO) {
            val favoriteCount =
                favoriteChannelDao.getFavoriteChannelCount(id = listId)

            val order = (favoriteCount + INT_VALUE_1).toLong()

            favoriteChannelDao.insertFavoriteChannel(
                FavoriteChannelEntity(
                    channelName = channel.channelName,
                    channelUrl = channel.channelUrl,
                    channelOrder = order,
                    parentListId = listId,
                ),
            )
        }
    }

    /*
        suspend fun updatePlaylistFavoriteChannels(channel: PlaylistChannel) {
            withContext(Dispatchers.IO) {
                favoriteChannelQueries.updateFavoriteChannelEntity(
                    channelName = channel.channelName,
                    channelUrl = channel.channelUrl,
                )
            }
        }
     */

    suspend fun updatePlaylistFavoriteChannels(channel: PlaylistChannel) {
        withContext(Dispatchers.IO) {
            favoriteChannelDao.updateFavoriteChannels(
                channelName = channel.channelName,
                channelUrl = channel.channelUrl,
            )
        }
    }

    /*    suspend fun deleteChannelFromFavorite(
            channelUrl: String,
            listId: Long,
        ) {
            withContext(Dispatchers.IO) {
                favoriteChannelQueries.deleteChannelFromFavorite(
                    id = listId,
                    channelUrl = channelUrl,
                )
            }
        }*/

    suspend fun deleteChannelFromFavorite(
        channelUrl: String,
        listId: Long,
    ) {
        favoriteChannelDao.deleteChannelFromFavorite(
            id = listId,
            channelUrl = channelUrl,
        )
    }

    /*    fun loadPlaylistFavoriteChannelUrls(listId: Long): List<String> {
            return favoriteChannelQueries.getPlaylistFavoriteChannelUrls(id = listId)
                .executeAsList()
        }*/

    suspend fun loadPlaylistFavoriteChannelUrls(listId: Long): List<String> {
        return favoriteChannelDao.getPlaylistFavoriteChannelUrls(id = listId)
    }

    /*
        fun loadFavoriteChannelUrls(): List<String> {
            return favoriteChannelQueries.getFavoriteChannelUrls()
                .executeAsList()
        }
     */

    suspend fun loadFavoriteChannelUrls(): List<String> {
        return favoriteChannelDao.getFavoriteChannelUrls()
    }

    /*    suspend fun deletePlaylistFavoriteChannels(listId: Long) {
            withContext(Dispatchers.IO) {
                favoriteChannelQueries.deletePlaylistFavoriteChannelEntities(id = listId)
            }
        }*/

    suspend fun deletePlaylistFavoriteChannels(listId: Long) {
        favoriteChannelDao.deletePlaylistFavoriteChannelEntities(id = listId)
    }
}
