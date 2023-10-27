/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 03.09.23, 21:48
 *
 */

package com.mvproject.tinyiptv.data.usecases

import com.mvproject.tinyiptv.data.mappers.EntityMapper.toTvPlaylistChannel
import com.mvproject.tinyiptv.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptv.data.repository.EpgProgramRepository
import com.mvproject.tinyiptv.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptv.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptv.data.repository.PreferenceRepository
import com.mvproject.tinyiptv.data.repository.SelectedEpgRepository
import com.mvproject.tinyiptv.utils.AppConstants
import com.mvproject.tinyiptv.utils.TimeUtils.actualDate

class GetGroupChannelsUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val favoriteChannelsRepository: FavoriteChannelsRepository,
    private val epgProgramRepository: EpgProgramRepository,
    private val selectedEpgRepository: SelectedEpgRepository
) {
    suspend operator fun invoke(
        group: String
    ): List<TvPlaylistChannel> {
        val currentPlaylistId = preferenceRepository.loadCurrentPlaylistId()

        val favorites = favoriteChannelsRepository
            .loadPlaylistFavoriteChannelUrls(listId = currentPlaylistId)

        val selectedEpgIds = selectedEpgRepository.getAllSelectedEpg()
            .map {
                it.channelEpgId
            }

        val epgs = epgProgramRepository.getEpgProgramsByIds(
            channelIds = selectedEpgIds,
            time = actualDate
        ).asSequence()

        val groupedEpgs = epgs.groupBy {
            it.channelId
        }

        // todo without android context
        // AppConstants.FOLDER_CHANNELS_ALL
        // AppConstants.FOLDER_CHANNELS_FAVORITE
        val channels = when (group) {
            AppConstants.FOLDER_CHANNELS_ALL -> {
                playlistChannelsRepository.loadPlaylistChannels(
                    listId = currentPlaylistId
                )
            }

            AppConstants.FOLDER_CHANNELS_FAVORITE -> {
                playlistChannelsRepository.loadPlaylistChannelsByUrls(
                    listId = currentPlaylistId,
                    urls = favorites
                )
            }

            else -> {
                playlistChannelsRepository.loadPlaylistGroupChannels(
                    listId = currentPlaylistId,
                    group = group
                )
            }
        }

        return channels.asSequence()
            .map { channel ->
                val isEpgRequired = channel.epgId in selectedEpgIds

                val epgList = if (isEpgRequired) {
                    groupedEpgs[channel.epgId] ?: emptyList()
                } else emptyList()

                channel.toTvPlaylistChannel(
                    isFavorite = channel.channelUrl in favorites,
                    isEpgUsing = isEpgRequired,
                    epgContent = epgList
                )
            }.toList()
    }
}