package com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.content

import com.mvproject.tinyiptvkmp.features.channels.api.data.parser.M3UParser.parseStringToChannels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM

internal class PlaylistContentLocalDataSourceImpl : PlaylistContentLocalDataSource {
    override suspend fun loadPlaylistContent(source: String): List<PlaylistChannelParseModel> =
        withContext(Dispatchers.IO) {
            val content = FileSystem.SYSTEM.read(source.toPath()) { readUtf8() }
            withContext(Dispatchers.Default) {
                parseStringToChannels(source = content)
            }
        }
}