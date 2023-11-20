/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 03.09.23, 21:42
 *
 */

package com.mvproject.tinyiptv.data.datasource

import com.mvproject.tinyiptv.data.mappers.ParseMappers
import com.mvproject.tinyiptv.data.model.channels.PlaylistChannel
import com.mvproject.tinyiptv.data.network.NetworkRepository
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