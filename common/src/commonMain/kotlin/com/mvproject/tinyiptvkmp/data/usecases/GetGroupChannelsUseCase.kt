/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 18:31
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.mappers.EntityMapper.toTvPlaylistChannel
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString
import tinyiptvkmp.common.generated.resources.Res
import tinyiptvkmp.common.generated.resources.channel_folder_all
import tinyiptvkmp.common.generated.resources.channel_folder_favorite

@OptIn(ExperimentalResourceApi::class)
class GetGroupChannelsUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val favoriteChannelsRepository: FavoriteChannelsRepository,
) {
    suspend operator fun invoke(group: String): List<TvPlaylistChannel> {
        val currentPlaylistId = preferenceRepository.loadCurrentPlaylistId()

        val favorites =
            favoriteChannelsRepository
                .loadPlaylistFavoriteChannelUrls(listId = currentPlaylistId)

        val channels =
            when (group) {
                getString(Res.string.channel_folder_all) -> {
                    playlistChannelsRepository.loadChannelsById(
                        listId = currentPlaylistId,
                    )
                }

                getString(Res.string.channel_folder_favorite) -> {
                    playlistChannelsRepository.loadPlaylistChannelsByUrls(
                        listId = currentPlaylistId,
                        urls = favorites,
                    )
                }

                else -> {
                    playlistChannelsRepository.loadPlaylistGroupChannels(
                        listId = currentPlaylistId,
                        group = group,
                    )
                }
            }

        return channels.asSequence()
            .map { channel ->
                channel.toTvPlaylistChannel(
                    isFavorite = channel.channelUrl in favorites,
                )
            }.toList()
    }
}
