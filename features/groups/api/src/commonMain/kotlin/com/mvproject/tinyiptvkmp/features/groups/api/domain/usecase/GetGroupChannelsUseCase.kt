package com.mvproject.tinyiptvkmp.features.groups.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelFavoriteRepository
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.PlaylistChannelRepository
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.ChannelGroupSelection
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
        playlistId: String,
        selection: ChannelGroupSelection,
    ) = withContext(Dispatchers.IO) {
        logger.d { "GetGroupChannelsUseCase selection = $selection" }

        val favorites = favoriteChannelsRepository
            .loadSelectedFavoriteChannels(playlistId = playlistId)
        val favoritesByUrl = favorites.associateBy { favorite -> favorite.channelUrl }

        val channels =
            when (selection) {
                ChannelGroupSelection.All -> {
                    playlistChannelRepository.loadChannelsById(playlistId = playlistId)
                }

                is ChannelGroupSelection.Favorite -> {
                    val urls = favorites
                        .asSequence()
                        .filter { favorite -> favorite.favoriteType == selection.type }
                        .map { favorite -> favorite.channelUrl }
                        .toList()

                    if (urls.isEmpty()) {
                        emptyList()
                    } else {
                        playlistChannelRepository.loadPlaylistChannelsByUrls(
                            playlistId = playlistId,
                            urls = urls,
                        )
                    }
                }

                is ChannelGroupSelection.Specified -> {
                    playlistChannelRepository.loadPlaylistGroupChannels(
                        playlistId = playlistId,
                        group = selection.groupName,
                    )
                }
            }

        channels
            .asSequence()
            .map { channel ->
                TvChannel(
                    channelName = channel.channelName,
                    channelLogo = channel.channelLogo,
                    channelUrl = channel.channelUrl,
                    programId = channel.programId,
                    favoriteType = favoritesByUrl[channel.channelUrl]?.favoriteType
                        ?: FavoriteType.NONE,
                )
            }.toList()
    }
}
