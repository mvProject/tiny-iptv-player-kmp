package com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelFavoriteRepository
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.PlaylistChannelRepository

interface ReplacePlaylistContentUseCase {
    suspend fun replaceLocalPlaylistContent(
        playlistId: String,
        source: String,
        clearExistingContentBeforeLoading: Boolean,
    ): Boolean

    suspend fun replaceRemotePlaylistContent(
        playlistId: String,
        source: String,
        clearExistingContentBeforeLoading: Boolean,
    ): Boolean
}

internal class ReplacePlaylistContentUseCaseImpl(
    private val playlistChannelRepository: PlaylistChannelRepository,
    private val channelFavoriteRepository: ChannelFavoriteRepository,
) : ReplacePlaylistContentUseCase {
    override suspend fun replaceLocalPlaylistContent(
        playlistId: String,
        source: String,
        clearExistingContentBeforeLoading: Boolean,
    ): Boolean =
        replacePlaylistContent(
            playlistId = playlistId,
            clearExistingContentBeforeLoading = clearExistingContentBeforeLoading,
            loadChannels = {
                playlistChannelRepository.loadLocalPlaylistChannels(
                    playlistId = playlistId,
                    source = source,
                )
            },
        )

    override suspend fun replaceRemotePlaylistContent(
        playlistId: String,
        source: String,
        clearExistingContentBeforeLoading: Boolean,
    ): Boolean =
        replacePlaylistContent(
            playlistId = playlistId,
            clearExistingContentBeforeLoading = clearExistingContentBeforeLoading,
            loadChannels = {
                playlistChannelRepository.loadRemotePlaylistChannels(
                    playlistId = playlistId,
                    source = source,
                )
            },
        )

    private suspend fun replacePlaylistContent(
        playlistId: String,
        clearExistingContentBeforeLoading: Boolean,
        loadChannels: suspend () -> List<PlaylistChannel>,
    ): Boolean {
        val channels =
            if (clearExistingContentBeforeLoading) {
                playlistChannelRepository.deletePlaylistChannels(listId = playlistId)
                loadChannels()
            } else {
                loadChannels()
            }

        if (channels.isEmpty()) {
            return false
        }

        if (!clearExistingContentBeforeLoading) {
            playlistChannelRepository.deletePlaylistChannels(listId = playlistId)
        }
        playlistChannelRepository.savePlaylistChannels(channels = channels)
        reconcileFavoriteChannels(
            playlistId = playlistId,
            channels = channels,
        )

        return true
    }

    private suspend fun reconcileFavoriteChannels(
        playlistId: String,
        channels: List<PlaylistChannel>,
    ) {
        val favoriteUrls =
            channelFavoriteRepository
                .loadFavoriteChannelUrls(playlistId = playlistId)
                .toSet()

        val favoriteNamesByUrl =
            buildMap {
                channels.forEach { channel ->
                    if (channel.channelUrl in favoriteUrls) {
                        put(channel.channelUrl, channel.channelName)
                    }
                }
            }

        channelFavoriteRepository.updateFavoriteChannels(
            playlistId = playlistId,
            channelNamesByUrl = favoriteNamesByUrl,
        )
    }
}
