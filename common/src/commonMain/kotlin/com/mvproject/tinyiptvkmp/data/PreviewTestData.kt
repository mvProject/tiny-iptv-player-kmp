/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 06.05.24, 11:36
 *
 */

package com.mvproject.tinyiptvkmp.data

import com.mvproject.tinyiptvkmp.data.model.channels.ChannelsGroup
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.data.model.epg.EpgProgram
import com.mvproject.tinyiptvkmp.data.model.playlist.Playlist
import com.mvproject.tinyiptvkmp.ui.screens.channels.data.TvPlaylistChannelEpg
import com.mvproject.tinyiptvkmp.utils.TimeUtils.actualDate
import kotlin.random.Random
import kotlin.time.Duration.Companion.minutes

object PreviewTestData {
    val testProgram =
        TvPlaylistChannel(
            channelName = "channelName",
            channelLogo = "",
            channelUrl = "",
            channelEpg = TvPlaylistChannelEpg(),
        )

    val testEpgProgram =
        EpgProgram(
            channelId = Random.nextLong().toString(),
            start = actualDate - 30.minutes.inWholeMilliseconds,
            stop = actualDate + 90.minutes.inWholeMilliseconds,
            title = "test title",
            description = "test description",
        )

    val testPlaylist =
        Playlist(
            id = Random.nextLong(),
            playlistTitle = "playlistTitle",
            playlistUrl = "playlistUrl",
            playlistLocalName = "playlistLocalName",
            lastUpdateDate = Random.nextLong(),
            updatePeriod = 3,
        )

    val testEpgPrograms =
        buildList {
            repeat(10) {
                add(
                    EpgProgram(
                        channelId = Random.nextLong().toString(),
                        start = actualDate + it * 30.minutes.inWholeMilliseconds,
                        stop = actualDate + (it + 1) * 30.minutes.inWholeMilliseconds,
                        title = "title $it",
                        description = "description $it",
                    ),
                )
            }
        }
    val testPlaylists =
        buildList {
            repeat(2) {
                add(
                    Playlist(
                        id = Random.nextLong(),
                        playlistTitle = "listName $it",
                        playlistUrl = "listUrl $it",
                        lastUpdateDate = Random.nextLong(),
                        playlistLocalName = "playlistLocalName",
                        updatePeriod = 3,
                    ),
                )
            }
        }

    val testChannelsGroups =
        buildList {
            repeat(10) {
                add(
                    ChannelsGroup(
                        groupName = "listName $it",
                        groupContentCount = it * (it + 1),
                    ),
                )
            }
        }
}
