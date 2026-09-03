package com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.content

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannel

internal interface PlaylistContentLocalDataSource {
    suspend fun loadPlaylistContent(
        playlistId: String,
        source: String,
    ): List<PlaylistChannel>
}
