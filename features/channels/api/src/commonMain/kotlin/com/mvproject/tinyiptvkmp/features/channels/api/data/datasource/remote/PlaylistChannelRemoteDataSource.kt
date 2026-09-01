package com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.remote

import com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.content.PlaylistChannelParseModel

internal interface PlaylistChannelRemoteDataSource {
    suspend fun loadPlaylistContent(url: String): List<PlaylistChannelParseModel>
}
