/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 20:26
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.enums.FavoriteType
import com.mvproject.tinyiptvkmp.data.enums.GroupType
import com.mvproject.tinyiptvkmp.data.mappers.EntityMapper.toTvPlaylistChannel
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.utils.KLog

class GetGroupChannelsUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val favoriteChannelsRepository: FavoriteChannelsRepository,
) {
    suspend operator fun invoke(
        group: String,
        groupType: String,
    ): List<TvPlaylistChannel> {
        val currentPlaylistId = preferenceRepository.loadCurrentPlaylistId()
        KLog.d("testing GetGroupChannelsUseCase group = $group, groupType = $groupType")

        val favorites =
            favoriteChannelsRepository
                .loadPlaylistFavoriteChannelUrls(listId = currentPlaylistId)

        val channels =
            when (groupType) {
                GroupType.SPECIFIED.name -> {
                    playlistChannelsRepository.loadPlaylistGroupChannels(
                        listId = currentPlaylistId,
                        group = group,
                    )
                }

                GroupType.FAVORITE.name -> {
                    val filtered = favorites.filter { it.type.name == group }
                    playlistChannelsRepository.loadPlaylistChannelsByUrls(
                        listId = currentPlaylistId,
                        urls = filtered.map { it.url },
                    )
                }
                else -> {
                    playlistChannelsRepository.loadChannelsById(
                        listId = currentPlaylistId,
                    )
                }
            }

        return channels
            .asSequence()
            .map { channel ->

                val favType = favorites.firstOrNull { it.url == channel.channelUrl }
                val type = favType?.type ?: FavoriteType.NONE

                channel.toTvPlaylistChannel(favoriteType = type)
            }.toList()
    }
}
