/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.mappers

import com.mvproject.tinyiptvkmp.data.model.channels.PlaylistChannel
import com.mvproject.tinyiptvkmp.data.model.epg.EpgProgram
import com.mvproject.tinyiptvkmp.data.model.response.EpgInfoResponse
import com.mvproject.tinyiptvkmp.data.model.response.EpgProgramResponse
import com.mvproject.tinyiptvkmp.data.parser.M3UParser
import com.mvproject.tinyiptvkmp.utils.AppConstants.INT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.utils.AppConstants.LONG_NO_VALUE
import com.mvproject.tinyiptvkmp.utils.AppConstants.LONG_VALUE_ZERO
import com.mvproject.tinyiptvkmp.utils.TimeUtils.actualDate
import kotlinx.datetime.Instant
import tinyiptvkmpdb.EpgInfoEntity
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.time.Duration.Companion.hours

object ParseMappers {
    fun EpgInfoResponse.toEpgInfoEntity() = with(this) {
        EpgInfoEntity(
            channelId = channelId,
            channelName = channelNames.trim(),
            channelLogo = channelIcon,
        )
    }

    /**
     * Maps List of [EpgProgramResponse] from Response to Entity
     * with id and filter for current day
     *
     * @param channelId id of channel for program
     *
     * @return the converted object
     */
    fun List<EpgProgramResponse>.asProgramEntities(
        channelId: String
    ): List<EpgProgram> {
        val actualDayFiltered = this.filterToActualDay()

        val programEndings = actualDayFiltered
            .map { programResponse -> programResponse.start }
            .calculateEndings()

        return actualDayFiltered.mapIndexed { index, item ->
            val endingTime = programEndings.elementAtOrNull(index)
                ?: item.start.toMillis().getLastItemEnding()
            item.asProgramEntity(
                channelId = channelId,
                endTime = endingTime
            )
        }
    }


    /**
     * Maps List of [EpgProgramResponse] from Response to Entity.
     * with id and end time
     *
     * @param channelId id of channel for program
     * @param endTime time in milliseconds for program end
     *
     * @return the converted object
     */
    private fun EpgProgramResponse.asProgramEntity(
        channelId: String,
        endTime: Long = LONG_VALUE_ZERO
    ) =
        with(this) {
            EpgProgram(
                start = start.toMillis(),
                stop = endTime,
                channelId = channelId,
                title = title,
                description = description
            )
        }

    /**
     * Filter List of [EpgProgramResponse] for current day programs start
     *
     * @return the filtered list
     */
    private fun List<EpgProgramResponse>.filterToActualDay(): List<EpgProgramResponse> {
        val actual = this.filter { programResponse ->
            programResponse.start.toMillis() > actualDate
        }
        return if (actual.count() > INT_VALUE_ZERO) actual else this
    }


    /**
     * Obtain time values of program end from start time of next
     *
     * @return the list of long values in milliseconds
     */
    fun List<String>.calculateEndings(): List<Long> {
        return buildList {
            this@calculateEndings.zipWithNext().forEach { timing ->
                add(timing.second.toMillis())
            }
        }
    }

    /**
     * Extension Method to non-null string variable which
     * convert date value to long value in milliseconds
     *
     * @return long value
     */
    fun String.toMillis(): Long {
        val parser = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return parser.parse(this)?.time ?: LONG_NO_VALUE
    }

    /**
     * Obtain time value of program end for last element
     * with hour duration from start time of current
     *
     * @return the long value in milliseconds
     */
    fun Long.getLastItemEnding() =
        (Instant.fromEpochMilliseconds(this) + NO_END_PROGRAM_DURATION.hours)
            .toEpochMilliseconds()

    fun parseStringToChannels(playlistId: Long, source: String): List<PlaylistChannel> {
        val parsed = M3UParser.parsePlaylist(source)
        val filtered = parsed.filter {
            it.mChannel.isNotEmpty() && it.mStreamURL.isNotEmpty()
        }
        val mappedResult = filtered.map { model ->
            PlaylistChannel(
                channelName = model.mChannel,
                channelLogo = model.mLogoURL,
                channelUrl = model.mStreamURL,
                channelGroup = model.mGroupTitle,
                parentListId = playlistId
            )
        }

        return mappedResult
    }


    private const val DATE_FORMAT = "dd-MM-yyyy HH:mm"
    private const val NO_END_PROGRAM_DURATION = 2
}