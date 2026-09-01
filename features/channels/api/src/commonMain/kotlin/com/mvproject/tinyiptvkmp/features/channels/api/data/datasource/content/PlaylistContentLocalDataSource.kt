package com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.content

internal interface PlaylistContentLocalDataSource {
    suspend fun loadPlaylistContent(source: String): List<PlaylistChannelParseModel>
}

