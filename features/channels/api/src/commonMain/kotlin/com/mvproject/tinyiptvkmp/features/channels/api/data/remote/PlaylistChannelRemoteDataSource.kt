package com.mvproject.tinyiptvkmp.features.channels.api.data.remote

import com.mvproject.tinyiptvkmp.features.channels.api.data.local.PlaylistChannelParseModel

internal interface PlaylistChannelRemoteDataSource {
    suspend fun loadPlaylistContent(url: String): List<PlaylistChannelParseModel>
}
