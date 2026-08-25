package com.mvproject.tinyiptvkmp.features.groups.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelFavoriteRepository
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.PlaylistChannelRepository
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.GroupType
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

class GetGroupChannelsUseCase(
    private val playlistChannelRepository: PlaylistChannelRepository,
    private val favoriteChannelsRepository: ChannelFavoriteRepository,
) : KoinComponent {
    private val logger by injectLogger()

    suspend operator fun invoke(
        group: String,
        groupType: String,
    ) = withContext(Dispatchers.IO) {
        logger.d { "testing GetGroupChannelsUseCase group = $group, groupType = $groupType" }

        val favorites = favoriteChannelsRepository
            .loadSelectedFavoriteChannels()

        val channels =
            when (groupType) {
                GroupType.SPECIFIED.name -> {
                    playlistChannelRepository.loadPlaylistGroupChannels(group = group)
                }

                GroupType.FAVORITE.name -> {
                    val filtered = favorites
                        .filter { it.favoriteType == group }
                        .map { it.channelUrl }

                    playlistChannelRepository.loadPlaylistChannelsByUrls(urls = filtered)
                }

                else -> {
                    playlistChannelRepository.loadChannelsById()
                }
            }

        channels
            .asSequence()
            .map { channel ->

                val favType = favorites.firstOrNull { it.channelUrl == channel.channelUrl }
                val type = favType?.favoriteType ?: FavoriteType.NONE.name

                TvChannel(
                    channelName = channel.channelName,
                    channelLogo = channel.channelLogo,
                    channelUrl = channel.channelUrl,
                    programId = channel.programId,
                    favoriteType = type,
                )
            }.toList()
    }
}
