package com.mvproject.tinyiptvkmp

import com.mvproject.tinyiptvkmp.core.foundation.utils.actualDate
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgProgram
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.withPrograms
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.ChannelsGroup
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.GroupType
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import kotlin.random.Random
import kotlin.time.Duration.Companion.minutes

object PreviewTestData {
    val testProgram =
        TvChannel(
            channelName = "channelName",
            channelLogo = "",
            channelUrl = "",
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

    val testProgramWithPrograms = testProgram.withPrograms(testEpgPrograms)

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