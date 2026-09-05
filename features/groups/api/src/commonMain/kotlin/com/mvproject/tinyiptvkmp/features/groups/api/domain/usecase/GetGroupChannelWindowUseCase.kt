package com.mvproject.tinyiptvkmp.features.groups.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.PlaylistChannelRepository
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.ChannelGroupSelection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

data class GroupChannelWindow(
    val channels: List<TvChannel>,
    val currentIndex: Int,
    val totalCount: Int,
) {
    val isEmpty: Boolean
        get() = channels.isEmpty()
}

interface GetGroupChannelWindowUseCase {
    suspend operator fun invoke(
        playlistId: String,
        selection: ChannelGroupSelection,
        channelUrl: String,
        before: Int = DEFAULT_NEIGHBOR_COUNT,
        after: Int = DEFAULT_NEIGHBOR_COUNT,
    ): GroupChannelWindow

    companion object {
        const val DEFAULT_NEIGHBOR_COUNT = 1
        const val NO_CURRENT_INDEX = -1
    }
}

class GetGroupChannelWindowUseCaseImpl(
    private val playlistChannelRepository: PlaylistChannelRepository,
) : GetGroupChannelWindowUseCase {
    override suspend operator fun invoke(
        playlistId: String,
        selection: ChannelGroupSelection,
        channelUrl: String,
        before: Int,
        after: Int,
    ): GroupChannelWindow = withContext(Dispatchers.IO) {
        val boundedBefore = before.coerceAtLeast(0)
        val boundedAfter = after.coerceAtLeast(0)
        val window =
            when (selection) {
                ChannelGroupSelection.All ->
                    playlistChannelRepository.loadPlaylistChannelWindowWithFavorites(
                        playlistId = playlistId,
                        channelUrl = channelUrl,
                        before = boundedBefore,
                        after = boundedAfter,
                    )

                is ChannelGroupSelection.Favorite ->
                    playlistChannelRepository.loadFavoritePlaylistChannelWindow(
                        playlistId = playlistId,
                        favoriteType = selection.type,
                        channelUrl = channelUrl,
                        before = boundedBefore,
                        after = boundedAfter,
                    )

                is ChannelGroupSelection.Specified ->
                    playlistChannelRepository.loadPlaylistGroupChannelWindowWithFavorites(
                        playlistId = playlistId,
                        group = selection.groupName,
                        channelUrl = channelUrl,
                        before = boundedBefore,
                        after = boundedAfter,
                    )
            }

        window
            ?.let { result ->
                GroupChannelWindow(
                    channels = result.channels,
                    currentIndex = result.currentIndex,
                    totalCount = result.totalCount,
                )
            }
            ?: GroupChannelWindow(
                channels = emptyList(),
                currentIndex = GetGroupChannelWindowUseCase.NO_CURRENT_INDEX,
                totalCount = 0,
            )
    }
}
