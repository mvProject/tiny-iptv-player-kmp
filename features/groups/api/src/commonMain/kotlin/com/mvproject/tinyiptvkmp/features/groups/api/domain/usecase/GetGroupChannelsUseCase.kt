package com.mvproject.tinyiptvkmp.features.groups.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.PlaylistChannelRepository
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.ChannelGroupSelection
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

data class GroupChannelsPage(
    val channels: List<TvChannel>,
    val nextOffset: Int,
    val hasMore: Boolean,
)

interface GetGroupChannelsUseCase {
    suspend operator fun invoke(
        playlistId: String,
        selection: ChannelGroupSelection,
        offset: Int = 0,
        limit: Int = DEFAULT_PAGE_SIZE,
        searchQuery: String = "",
    ): GroupChannelsPage

    companion object {
        const val DEFAULT_PAGE_SIZE = 100
    }
}

class GetGroupChannelsUseCaseImpl(
    private val playlistChannelRepository: PlaylistChannelRepository,
) : GetGroupChannelsUseCase, KoinComponent {
    private val logger by injectLogger()

    override suspend operator fun invoke(
        playlistId: String,
        selection: ChannelGroupSelection,
        offset: Int,
        limit: Int,
        searchQuery: String,
    ) = withContext(Dispatchers.IO) {
        val boundedLimit = limit.coerceAtLeast(0)
        if (boundedLimit == 0) {
            return@withContext GroupChannelsPage(
                channels = emptyList(),
                nextOffset = offset.coerceAtLeast(0),
                hasMore = false,
            )
        }

        val boundedOffset = offset.coerceAtLeast(0)
        val queryLimit =
            if (boundedLimit == Int.MAX_VALUE) {
                Int.MAX_VALUE
            } else {
                boundedLimit + 1
            }
        val normalizedSearchQuery = searchQuery.trim()

        logger.d {
            "GetGroupChannelsUseCase selection = $selection, offset = $boundedOffset, " +
                    "limit = $boundedLimit, searchQuery = $normalizedSearchQuery"
        }

        val channelsPage =
            when (selection) {
                ChannelGroupSelection.All -> {
                    playlistChannelRepository.loadPlaylistChannelsWithFavorites(
                        playlistId = playlistId,
                        offset = boundedOffset,
                        limit = queryLimit,
                        searchQuery = normalizedSearchQuery,
                    )
                }

                is ChannelGroupSelection.Favorite -> {
                    playlistChannelRepository.loadFavoritePlaylistChannels(
                        playlistId = playlistId,
                        favoriteType = selection.type,
                        offset = boundedOffset,
                        limit = queryLimit,
                        searchQuery = normalizedSearchQuery,
                    )
                }

                is ChannelGroupSelection.Specified -> {
                    playlistChannelRepository.loadPlaylistGroupChannelsWithFavorites(
                        playlistId = playlistId,
                        group = selection.groupName,
                        offset = boundedOffset,
                        limit = queryLimit,
                        searchQuery = normalizedSearchQuery,
                    )
                }
            }

        val hasMore = channelsPage.size > boundedLimit
        val visibleChannels = channelsPage.take(boundedLimit)
        GroupChannelsPage(
            channels = visibleChannels,
            nextOffset = boundedOffset + visibleChannels.size,
            hasMore = hasMore,
        )
    }
}
