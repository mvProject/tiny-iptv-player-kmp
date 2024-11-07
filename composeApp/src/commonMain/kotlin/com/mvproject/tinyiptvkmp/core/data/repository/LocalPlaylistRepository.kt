/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.core.data.repository

import com.mvproject.tinyiptvkmp.core.data.model.PlaylistChannelParseModel
import com.mvproject.tinyiptvkmp.core.data.parser.M3UParser.parseStringToChannels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.FileSystem
import okio.Path.Companion.toPath

class LocalPlaylistRepository {
    suspend fun getFromLocalPlaylist(source: String): List<PlaylistChannelParseModel> =
        withContext(Dispatchers.Default) {
            val path = source.toPath()
            val content =
                FileSystem.SYSTEM.read(path) {
                    readUtf8()
                }

            parseStringToChannels(source = content)
        }
}
