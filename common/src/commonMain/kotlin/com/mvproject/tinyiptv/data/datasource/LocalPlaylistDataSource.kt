/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 27.10.23, 16:59
 *
 */

package com.mvproject.tinyiptv.data.datasource

import com.mvproject.tinyiptv.data.model.channels.PlaylistChannel

class LocalPlaylistDataSource() {
    // private val context: Context
    // @SuppressLint("Recycle")
    fun getFromLocalPlaylist(
        playlistId: Long,
        uri: String
    ): List<PlaylistChannel> {
        // todo fix local load
        // todo parse to Uri
        // uri = Uri.parse(source)

        /*val file = context.contentResolver
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
                    val channels = filtered.map { model ->
                        PlaylistChannel(
                            channelName = model.mChannel,
                            channelLogo = model.mLogoURL,
                            channelUrl = model.mStreamURL,
                            channelGroup = model.mGroupTitle,
                            parentListId = playlistId
                        )
                    }

                    *//*          val channels = ParseMappers.parseStringToChannels(
                                  playlistId = playlistId,
                                  source = content
                              )*//*

                    addAll(channels)
                }
            }
        }*/
        return emptyList()
    }

    private companion object {
        const val MODE_READ_ONLY = "r"
    }
}