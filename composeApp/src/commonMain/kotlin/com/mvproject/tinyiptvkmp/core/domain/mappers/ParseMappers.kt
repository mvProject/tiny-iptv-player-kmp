/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 27.06.24, 14:40
 *
 */

package com.mvproject.tinyiptvkmp.core.domain.mappers

import com.mvproject.tinyiptvkmp.core.data.model.PlaylistChannel
import com.mvproject.tinyiptvkmp.core.data.parser.M3UParser

object ParseMappers {
    fun parseStringToChannels(
        playlistId: Long,
        source: String,
    ): List<PlaylistChannel> {
        val parsed = M3UParser.parsePlaylist(source)
        val filtered =
            parsed.filter {
                it.channel.isNotEmpty() && it.streamURL.isNotEmpty()
            }
        val mappedResult =
            filtered.map { model ->
                PlaylistChannel(
                    channelName = model.channel,
                    channelLogo = model.logoURL,
                    channelUrl = model.streamURL,
                    channelGroup = model.groupTitle,
                    parentListId = playlistId,
                )
            }

        return mappedResult
    }
}
