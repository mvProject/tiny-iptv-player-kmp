/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.datasource

import com.mvproject.tinyiptvkmp.data.mappers.ParseMappers
import com.mvproject.tinyiptvkmp.data.model.channels.PlaylistChannel
import com.mvproject.tinyiptvkmp.data.network.NetworkRepository
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemotePlaylistDataSource(
    private val networkRepository: NetworkRepository,
) {
    suspend fun getFromRemotePlaylist(
        playlistId: Long,
        url: String,
    ): List<PlaylistChannel> =
        withContext(Dispatchers.Default) {
            val response = networkRepository.loadPlaylistData(url)
            val content = response.bodyAsText()

            ParseMappers.parseStringToChannels(
                playlistId = playlistId,
                source = content,
            )
        }
}
