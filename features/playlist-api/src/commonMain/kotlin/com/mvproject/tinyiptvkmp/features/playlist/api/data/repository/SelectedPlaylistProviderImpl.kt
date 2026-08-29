package com.mvproject.tinyiptvkmp.features.playlist.api.data.repository

import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.SelectedPlaylistProvider
import com.mvproject.tinyiptvkmp.features.playlist.api.data.local.PlaylistLocalDataSource

internal class SelectedPlaylistProviderImpl(
    private val local: PlaylistLocalDataSource,
) : SelectedPlaylistProvider {
    override suspend fun getSelectedPlaylistId(): String =
        local
            .getSelectedPlaylistId()
            .orEmpty()
}
