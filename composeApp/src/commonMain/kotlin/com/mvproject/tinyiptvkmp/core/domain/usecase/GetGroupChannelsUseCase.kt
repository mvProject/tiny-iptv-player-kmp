package com.mvproject.tinyiptvkmp.core.domain.usecase

import com.mvproject.tinyiptvkmp.core.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.core.domain.enums.FavoriteType
import com.mvproject.tinyiptvkmp.core.domain.enums.GroupType
import com.mvproject.tinyiptvkmp.core.domain.mappers.Mapper.toFavType
import com.mvproject.tinyiptvkmp.core.domain.mappers.Mapper.toTvChannel
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

class GetGroupChannelsUseCase(
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val favoriteChannelsRepository: FavoriteChannelsRepository,
) : KoinComponent {
    private val logger by injectLogger()

    suspend operator fun invoke(
        group: String,
        groupType: String,
    ) = withContext(Dispatchers.IO) {
        logger.d { "testing GetGroupChannelsUseCase group = $group, groupType = $groupType" }

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

                channel.toTvChannel(favoriteType = type)
            }.toList()
    }
}
