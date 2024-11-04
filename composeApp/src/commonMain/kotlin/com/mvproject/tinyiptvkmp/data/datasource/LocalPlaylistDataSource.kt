/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.datasource

import com.mvproject.tinyiptvkmp.data.mappers.ParseMappers
import com.mvproject.tinyiptvkmp.data.model.channels.PlaylistChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.FileSystem
import okio.Path.Companion.toPath

class LocalPlaylistDataSource {
    suspend fun getFromLocalPlaylist(
        playlistId: Long,
        source: String,
    ): List<PlaylistChannel> =
        withContext(Dispatchers.Default) {
            val path = source.toPath()
            val content =
                FileSystem.SYSTEM.read(path) {
                    readUtf8()
                }

            ParseMappers.parseStringToChannels(
                playlistId = playlistId,
                source = content,
            )
        }
}
