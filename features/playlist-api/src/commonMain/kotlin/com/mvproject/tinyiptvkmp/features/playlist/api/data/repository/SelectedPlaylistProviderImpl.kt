package com.mvproject.tinyiptvkmp.features.playlist.api.data.repository

import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.SelectedPlaylistProvider
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistRepository

internal class SelectedPlaylistProviderImpl(
    private val playlistRepository: PlaylistRepository,
) : SelectedPlaylistProvider {
    override suspend fun getSelectedPlaylistId(): String =
        playlistRepository
            .getAllPlaylists()
            .firstOrNull { playlist -> playlist.isSelected }
            ?.id
            .orEmpty()
}
