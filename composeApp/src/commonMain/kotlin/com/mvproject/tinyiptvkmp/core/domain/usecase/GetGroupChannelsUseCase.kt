package com.mvproject.tinyiptvkmp.core.domain.usecase

import com.mvproject.tinyiptvkmp.core.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.core.domain.mappers.EntityMapper.toFavType
import com.mvproject.tinyiptvkmp.core.domain.mappers.EntityMapper.toTvPlaylistChannel
import com.mvproject.tinyiptvkmp.data.enums.FavoriteType
import com.mvproject.tinyiptvkmp.data.enums.GroupType
import com.mvproject.tinyiptvkmp.utils.KLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetGroupChannelsUseCase(
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val favoriteChannelsRepository: FavoriteChannelsRepository,
) {
    suspend operator fun invoke(
        group: String,
        groupType: String,
    ) = withContext(Dispatchers.IO) {
        KLog.d("testing GetGroupChannelsUseCase group = $group, groupType = $groupType")

        val favorites = favoriteChannelsRepository
            .loadSelectedFavoriteChannels()
            .map { item -> item.toFavType() }

        val channels =
            when (groupType) {
                GroupType.SPECIFIED.name -> {
                    playlistChannelsRepository.loadPlaylistGroupChannels(group = group)
                }

                GroupType.FAVORITE.name -> {
                    val filtered = favorites
                        .filter { it.type.name == group }
                        .map { it.url }

                    playlistChannelsRepository.loadPlaylistChannelsByUrls(urls = filtered)
                }

                else -> {
                    playlistChannelsRepository.loadChannelsById()
                }
            }

        channels
            .asSequence()
            .map { channel ->

                val favType = favorites.firstOrNull { it.url == channel.channelUrl }
                val type = favType?.type ?: FavoriteType.NONE

                channel.toTvPlaylistChannel(favoriteType = type)
            }.toList()
    }
}