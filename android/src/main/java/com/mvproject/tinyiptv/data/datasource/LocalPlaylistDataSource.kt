/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 03.09.23, 21:42
 *
 */

package com.mvproject.tinyiptv.data.datasource

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import com.mvproject.tinyiptv.data.model.channels.PlaylistChannel
import com.mvproject.tinyiptv.data.parser.M3UParser
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

class LocalPlaylistDataSource(
    private val context: Context
) {
    @SuppressLint("Recycle")
    fun getFromLocalPlaylist(
        playlistId: Long,
        uri: Uri
    ): List<PlaylistChannel> {
        val file = context.contentResolver
            .openFileDescriptor(uri, MODE_READ_ONLY)
            ?.fileDescriptor

        return buildList {
            BufferedReader(
                InputStreamReader(
                    FileInputStream(file)
                )
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

    private companion object {
        const val MODE_READ_ONLY = "r"
    }
}