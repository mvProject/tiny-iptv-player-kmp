package com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist

interface PlaylistContentCoordinator {
    suspend fun createPlaylistWithContent(playlist: Playlist)

    suspend fun updatePlaylistWithContent(playlist: Playlist)

    suspend fun deletePlaylistWithContent(playlist: Playlist)

    suspend fun refreshRemotePlaylistContent()
}
