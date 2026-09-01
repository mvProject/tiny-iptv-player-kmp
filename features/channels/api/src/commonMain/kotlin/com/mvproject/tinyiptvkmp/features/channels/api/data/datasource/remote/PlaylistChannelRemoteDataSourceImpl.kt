package com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.remote

import com.mvproject.tinyiptvkmp.core.network.datasource.NetworkPlaylistDatasource
import com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.content.PlaylistChannelParseModel
import com.mvproject.tinyiptvkmp.features.channels.api.data.parser.M3UParser.parseStringToChannels
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class PlaylistChannelRemoteDataSourceImpl(
    private val network: NetworkPlaylistDatasource,
) : PlaylistChannelRemoteDataSource {
    override suspend fun loadPlaylistContent(url: String): List<PlaylistChannelParseModel> {
        val content = network.loadPlaylistData(url).bodyAsText()

        return withContext(Dispatchers.Default) {
            parseStringToChannels(source = content)
        }
    }
}
