/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 03.09.23, 21:42
 *
 */

package com.mvproject.tinyiptv.data.datasource

import com.mvproject.tinyiptv.data.models.channels.PlaylistChannel
import com.mvproject.tinyiptv.data.network.NetworkRepository
import com.mvproject.tinyiptv.data.parser.M3UParser
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
            BufferedReader(
                InputStreamReader(resultStream)
            ).use { reader ->
                reader.readText().also { content ->
                    val parsed = M3UParser.parsePlaylist(content)
                    val filtered = parsed.filter {
                        it.mChannel.isNotEmpty() && it.mStreamURL.isNotEmpty()
                    }
                    val mapped = filtered.map { model ->
                        PlaylistChannel(
                            channelName = model.mChannel,
                            channelLogo = model.mLogoURL,
                            channelUrl = model.mStreamURL,
                            channelGroup = model.mGroupTitle,
                            parentListId = playlistId
                        )
                    }
                    addAll(mapped)
                }
            }
        }
    }
}