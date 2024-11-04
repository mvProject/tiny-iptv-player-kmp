/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 27.06.24, 14:40
 *
 */

package com.mvproject.tinyiptvkmp.data.mappers

import com.mvproject.tinyiptvkmp.data.model.channels.PlaylistChannel
import com.mvproject.tinyiptvkmp.data.parser.M3UParser

object ParseMappers {
    fun parseStringToChannels(
        playlistId: Long,
        source: String,
    ): List<PlaylistChannel> {
        val parsed = M3UParser.parsePlaylist2(source)
        val filtered =
            parsed.filter {
                it.mChannel.isNotEmpty() && it.mStreamURL.isNotEmpty()
            }
        val mappedResult =
            filtered.map { model ->
                PlaylistChannel(
                    channelName = model.mChannel,
                    channelLogo = model.mLogoURL,
                    channelUrl = model.mStreamURL,
                    channelGroup = model.mGroupTitle,
                    parentListId = playlistId,
                )
            }

        return mappedResult
    }
}
