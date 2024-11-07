/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.core.data.repository

import com.mvproject.tinyiptvkmp.core.data.model.PlaylistChannel
import com.mvproject.tinyiptvkmp.core.domain.mappers.ParseMappers
import com.mvproject.tinyiptvkmp.core.network.datasource.NetworkPlaylistDatasource
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemotePlaylistRepository(
    private val networkPlaylistDatasource: NetworkPlaylistDatasource,
) {
    suspend fun getFromRemotePlaylist(
        playlistId: Long,
        url: String,
    ): List<PlaylistChannel> =
        withContext(Dispatchers.Default) {
            val response = networkPlaylistDatasource.loadPlaylistData(url)
            val content = response.bodyAsText()

            ParseMappers.parseStringToChannels(
                playlistId = playlistId,
                source = content,
            )
        }
}
