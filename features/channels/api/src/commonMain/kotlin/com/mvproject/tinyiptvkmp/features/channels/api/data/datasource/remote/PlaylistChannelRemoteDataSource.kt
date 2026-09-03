package com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.remote

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannel

internal interface PlaylistChannelRemoteDataSource {
    suspend fun loadPlaylistContent(
        playlistId: String,
        url: String,
    ): List<PlaylistChannel>
}
