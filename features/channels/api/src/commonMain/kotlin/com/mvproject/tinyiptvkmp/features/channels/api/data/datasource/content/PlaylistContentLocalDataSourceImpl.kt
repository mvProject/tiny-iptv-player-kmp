package com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.content

import com.mvproject.tinyiptvkmp.features.channels.api.data.parser.M3UParser.parseLinesToChannels
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM

internal class PlaylistContentLocalDataSourceImpl : PlaylistContentLocalDataSource {
    override suspend fun loadPlaylistContent(
        playlistId: String,
        source: String,
    ): List<PlaylistChannel> =
        withContext(Dispatchers.IO) {
            FileSystem.SYSTEM.read(source.toPath()) {
                parseLinesToChannels(
                    playlistId = playlistId,
                    lines = generateSequence { readUtf8Line() },
                )
            }
        }
}
