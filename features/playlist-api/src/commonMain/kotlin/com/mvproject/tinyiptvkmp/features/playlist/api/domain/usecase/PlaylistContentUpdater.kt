package com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannelSource
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannelSourceType
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelFavoriteRepository
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.PlaylistChannelRepository
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.PlaylistType

internal class PlaylistContentUpdater(
    private val playlistChannelRepository: PlaylistChannelRepository,
    private val channelFavoriteRepository: ChannelFavoriteRepository,
) {
    suspend fun replacePlaylistContent(
        playlist: Playlist,
        deleteBeforeLoad: Boolean,
    ): Boolean {
        val channels =
            if (deleteBeforeLoad) {
                playlistChannelRepository.deletePlaylistChannels(listId = playlist.id)
                playlistChannelRepository.loadPlaylistChannels(playlist.toChannelSource())
            } else {
                playlistChannelRepository.loadPlaylistChannels(playlist.toChannelSource())
            }

        if (channels.isEmpty()) {
            return false
        }

        if (!deleteBeforeLoad) {
            playlistChannelRepository.deletePlaylistChannels(listId = playlist.id)
        }
        playlistChannelRepository.savePlaylistChannels(channels = channels)
        reconcileFavoriteChannels(
            playlistId = playlist.id,
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

private fun Playlist.toChannelSource() =
    PlaylistChannelSource(
        parentListId = id,
        source = playlistSource,
        sourceType = when (playlistType) {
            PlaylistType.LOCAL -> PlaylistChannelSourceType.LOCAL
            PlaylistType.REMOTE -> PlaylistChannelSourceType.REMOTE
        },
    )
