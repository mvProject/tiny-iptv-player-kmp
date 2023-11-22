/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.repository

import com.mvproject.tinyiptvkmp.TinyIptvKmpDatabase
import com.mvproject.tinyiptvkmp.data.model.channels.PlaylistChannel
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.utils.AppConstants.INT_VALUE_1
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tinyiptvkmpdb.FavoriteChannelEntity

class FavoriteChannelsRepository(private val db: TinyIptvKmpDatabase) {
    private val favoriteChannelQueries = db.favoriteChannelEntityQueries

    suspend fun addChannelToFavorite(channel: TvPlaylistChannel, listId: Long) {
        withContext(Dispatchers.IO) {
            val favoriteCount = favoriteChannelQueries.getPlaylistFavoriteChannelCount(id = listId)
                .executeAsOne()
                .toInt()

            val order = (favoriteCount + INT_VALUE_1).toLong()

            favoriteChannelQueries.addFavoriteChannelEntity(
                FavoriteChannelEntity(
                    channelName = channel.channelName,
                    channelUrl = channel.channelUrl,
                    channelOrder = order,
                    parentListId = listId
                )
            )
        }
    }

    suspend fun updatePlaylistFavoriteChannels(channel: PlaylistChannel) {
        withContext(Dispatchers.IO) {
            favoriteChannelQueries.updateFavoriteChannelEntity(
                channelName = channel.channelName,
                channelUrl = channel.channelUrl
            )
        }
    }

    suspend fun deleteChannelFromFavorite(channelUrl: String, listId: Long) {
        withContext(Dispatchers.IO) {
            favoriteChannelQueries.deleteChannelFromFavorite(
                id = listId,
                channelUrl = channelUrl
            )
        }
    }

    fun loadPlaylistFavoriteChannelUrls(listId: Long): List<String> {
        return favoriteChannelQueries.getPlaylistFavoriteChannelUrls(id = listId)
            .executeAsList()
    }

    fun loadFavoriteChannelUrls(): List<String> {
        return favoriteChannelQueries.getFavoriteChannelUrls()
            .executeAsList()
    }

    suspend fun deletePlaylistFavoriteChannels(listId: Long) {
        withContext(Dispatchers.IO) {
            favoriteChannelQueries.deletePlaylistFavoriteChannelEntities(id = listId)
        }
    }
}