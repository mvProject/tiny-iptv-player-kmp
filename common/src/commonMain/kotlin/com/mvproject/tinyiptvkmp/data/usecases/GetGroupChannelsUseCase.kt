/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.03.24, 10:49
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.mappers.EntityMapper.toTvPlaylistChannel
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.data.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.data.repository.SelectedEpgRepository
import com.mvproject.tinyiptvkmp.utils.TimeUtils.actualDate
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
    private val epgProgramRepository: EpgProgramRepository,
    private val selectedEpgRepository: SelectedEpgRepository,
) {
    suspend operator fun invoke(group: String): List<TvPlaylistChannel> {
        val currentPlaylistId = preferenceRepository.loadCurrentPlaylistId()

        val favorites =
            favoriteChannelsRepository
                .loadPlaylistFavoriteChannelUrls(listId = currentPlaylistId)

        val selectedEpgIds =
            selectedEpgRepository.getAllSelectedEpg()
                .map {
                    it.channelEpgId
                }

        val epgs =
            epgProgramRepository.getEpgProgramsByIds(
                channelIds = selectedEpgIds,
                time = actualDate,
            ).asSequence()

        val groupedEpgs =
            epgs.groupBy {
                it.channelId
            }

        val channels =
            when (group) {
                getString(Res.string.channel_folder_all) -> {
                    playlistChannelsRepository.loadPlaylistChannels(
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
                val isEpgRequired = channel.epgId in selectedEpgIds

                val epgList =
                    if (isEpgRequired) {
                        groupedEpgs[channel.epgId] ?: emptyList()
                    } else {
                        emptyList()
                    }

                channel.toTvPlaylistChannel(
                    isFavorite = channel.channelUrl in favorites,
                    isEpgUsing = isEpgRequired,
                    epgContent = epgList,
                )
            }.toList()
    }
}
