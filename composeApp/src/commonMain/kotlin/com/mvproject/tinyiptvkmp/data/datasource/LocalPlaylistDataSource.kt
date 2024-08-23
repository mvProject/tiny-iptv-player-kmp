/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.datasource

import com.mvproject.tinyiptvkmp.data.mappers.ParseMappers
import com.mvproject.tinyiptvkmp.data.model.channels.PlaylistChannel
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import kotlin.io.path.Path

class LocalPlaylistDataSource {
    fun getFromLocalPlaylist(
        playlistId: Long,
        source: String,
    ): List<PlaylistChannel> {
        val file = Path(source).toFile()
        return buildList {
            InputStreamReader(FileInputStream(file), Charsets.UTF_8).use { inputStreamReader ->
                BufferedReader(inputStreamReader).use { bufferedReader ->
                    bufferedReader.readText().also { content ->

                        val channels =
                            ParseMappers.parseStringToChannels(
                                playlistId = playlistId,
                                source = content,
                            )

                        addAll(channels)
                    }
                }
            }
        }
    }
}
