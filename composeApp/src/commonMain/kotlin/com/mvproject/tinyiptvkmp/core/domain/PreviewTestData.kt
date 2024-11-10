/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 26.07.24, 12:57
 *
 */

package com.mvproject.tinyiptvkmp.core.domain

import com.mvproject.tinyiptvkmp.core.common.utils.TimeUtils.actualDate
import com.mvproject.tinyiptvkmp.core.domain.enums.GroupType
import com.mvproject.tinyiptvkmp.core.domain.model.ChannelsGroup
import com.mvproject.tinyiptvkmp.core.domain.model.EpgProgram
import com.mvproject.tinyiptvkmp.core.domain.model.Playlist
import com.mvproject.tinyiptvkmp.core.domain.model.TvChannel
import kotlin.random.Random
import kotlin.time.Duration.Companion.minutes

object PreviewTestData {
    val testProgram =
        TvChannel(
            channelName = "channelName",
            channelLogo = "",
            channelUrl = "",
            programs = emptyList(),
        )

    val testEpgProgram =
        EpgProgram(
            programId = "1",
            channelId = Random.nextLong().toString(),
            dateTimeStart = actualDate - 30.minutes.inWholeMilliseconds,
            dateTimeEnd = actualDate + 90.minutes.inWholeMilliseconds,
            title = "test title",
            description = "test description",
        )

    val testPlaylist =
        Playlist(
            id = Random.nextLong().toString(),
            playlistName = "playlistTitle",
            playlistSource = "playlistUrl",
            lastUpdateDate = Random.nextLong(),
            updatePeriod = 3,
        )

    val testEpgPrograms =
        buildList {
            repeat(4) {
                add(
                    EpgProgram(
                        programId = "1",
                        channelId = Random.nextLong().toString(),
                        dateTimeStart = actualDate + it * 30.minutes.inWholeMilliseconds,
                        dateTimeEnd = actualDate + (it + 1) * 30.minutes.inWholeMilliseconds,
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
                        id = Random.nextLong().toString(),
                        playlistName = "listName $it",
                        playlistSource = "listUrl $it",
                        lastUpdateDate = Random.nextLong(),
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
                        groupType = GroupType.SPECIFIED,
                        groupContentCount = it * (it + 1),
                    ),
                )
            }
        }
}
