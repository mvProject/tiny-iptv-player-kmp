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
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import java.io.BufferedReader
import java.io.InputStreamReader

class RemotePlaylistDataSource(
    private val networkRepository: NetworkRepository
) {
    suspend fun getFromRemotePlaylist(
        playlistId: Long,
        url: String
    ): List<PlaylistChannel> {
        val resultStream = networkRepository.loadPlaylistData(url)
            .bodyAsChannel()
            .toInputStream()

        return buildList {
            InputStreamReader(resultStream, Charsets.UTF_8).use { inputStreamReader ->
                BufferedReader(inputStreamReader).use { bufferedReader ->
                    bufferedReader.readText().also { content ->

                        val channels = ParseMappers.parseStringToChannels(
                            playlistId = playlistId,
                            source = content
                        )

                        addAll(channels)
                    }
                }
            }
        }
    }
}