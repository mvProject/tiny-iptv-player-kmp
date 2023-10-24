/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.data.mappers

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.mvproject.tinyiptv.data.models.channels.TvPlaylistChannel
import com.mvproject.tinyiptv.data.models.epg.EpgProgram
import com.mvproject.tinyiptv.utils.TimeUtils.actualDate

object ListMappers {
    fun List<TvPlaylistChannel>.createMediaItems(): List<MediaItem> {
        return buildList {
            this@createMediaItems.forEach { video ->
                add(
                    MediaItem.Builder()
                        .setUri(video.channelUrl)
                        //   .setMimeType(MimeTypes.APPLICATION_M3U8)
                        .setMediaMetadata(
                            MediaMetadata.Builder()
                                .setDisplayTitle(video.channelName)
                                .build()
                        ).build()
                )
            }
        }
    }

    fun List<EpgProgram>.toActual(): List<EpgProgram> {
        return this.filter { it.stop > actualDate }
    }

    fun List<TvPlaylistChannel>.withRefreshedEpg(): List<TvPlaylistChannel> {
        return this.map {
            val epg = it.channelEpg.toActual()
            it.copy(channelEpg = epg)
        }
    }
}