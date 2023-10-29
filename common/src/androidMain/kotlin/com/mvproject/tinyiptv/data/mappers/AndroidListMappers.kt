/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 26.10.23, 15:44
 *
 */

package com.mvproject.tinyiptv.data.mappers

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.mvproject.tinyiptv.data.model.channels.TvPlaylistChannel

object AndroidListMappers {
    fun List<TvPlaylistChannel>.createMediaItems(): List<MediaItem> {
        return buildList {
            this@createMediaItems.forEach { video ->
                add(
                    MediaItem.Builder()
                        .setUri(video.channelUrl)
                        .setMediaMetadata(
                            MediaMetadata.Builder()
                                .setDisplayTitle(video.channelName)
                                .build()
                        ).build()
                )
            }
        }
    }
}