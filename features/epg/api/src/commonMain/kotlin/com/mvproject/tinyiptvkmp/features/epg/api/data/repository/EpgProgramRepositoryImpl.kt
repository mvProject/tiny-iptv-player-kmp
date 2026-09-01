/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 17.05.24, 18:15
 *
 */

package com.mvproject.tinyiptvkmp.features.epg.api.data.repository

import com.mvproject.tinyiptvkmp.core.network.data.response.EpgProgramResponse
import com.mvproject.tinyiptvkmp.features.epg.api.data.local.EpgProgramLocalDataSource
import com.mvproject.tinyiptvkmp.features.epg.api.data.mapper.EpgMappers.toEpgProgram
import com.mvproject.tinyiptvkmp.features.epg.api.data.mapper.EpgMappers.toEpgProgramEntity
import com.mvproject.tinyiptvkmp.features.epg.api.data.remote.EpgProgramRemoteDataSource
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgProgram
import com.mvproject.tinyiptvkmp.features.epg.api.domain.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.parseEpgInstant
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import org.koin.core.component.KoinComponent

internal class EpgProgramRepositoryImpl(
    private val localDataSource: EpgProgramLocalDataSource,
    private val remoteDataSource: EpgProgramRemoteDataSource,
) : EpgProgramRepository,
    KoinComponent {
    private val logger by injectLogger()

    override suspend fun getEpgProgramsByIds(
        channelIds: List<String>,
        time: Long,
    ): List<EpgProgram> =
        localDataSource
            .getPrograms(channelIds = channelIds, time = time)
            .map { it.toEpgProgram() }

    override suspend fun getEpgProgramsById(
        channelId: String,
        time: Long,
    ): List<EpgProgram> =
        localDataSource
            .getProgram(channelId = channelId, time = time)
            .map { it.toEpgProgram() }

    override suspend fun cleanProgramsBeforeDate(date: Long) {
        val deleted = localDataSource.cleanProgramsBeforeDate(date = date)
        logger.e { "testing cleanProgramsBeforeDate deleted=$deleted" }
    }

    override suspend fun updateProgramsFromSource(
        sourceUrl: String,
        currentDate: Long,
        onChannelProgramsUpdated: () -> Unit,
    ): Int {
        var programmeCount = 0
        val programs = mutableListOf<EpgProgramResponse>()
        var currentId = ""

        remoteDataSource.downloadAndParseXml(
            url = sourceUrl,
            onProgrammeParsed = { programme ->
                val start = parseEpgInstant(programme.start)
                val end = parseEpgInstant(programme.stop)

                if (end >= currentDate) {
                    programmeCount++
                    val epgProgramResponse =
                        EpgProgramResponse(
                            dateTimeStart = start,
                            dateTimeEnd = end,
                            title = programme.title,
                            description = programme.desc.orEmpty(),
                        )

                    if (currentId.isBlank()) {
                        currentId = programme.channel
                        onChannelProgramsUpdated()
                        programs.add(epgProgramResponse)
                    } else if (programme.channel == currentId) {
                        programs.add(epgProgramResponse)
                    } else {
                        if (programs.isNotEmpty()) {
                            replacePrograms(
                                channelId = currentId,
                                programs = programs,
                            )
                        }
                        programs.clear()
                        currentId = programme.channel
                        onChannelProgramsUpdated()
                        programs.add(epgProgramResponse)
                    }
                }
            },
        )

        if (programs.isNotEmpty()) {
            replacePrograms(
                channelId = currentId,
                programs = programs,
            )
        }

        return programmeCount
    }

    private suspend fun replacePrograms(
        channelId: String,
        programs: List<EpgProgramResponse>,
    ) {
        localDataSource.replacePrograms(
            channelId = channelId,
            programs = programs.map { it.toEpgProgramEntity(channelId = channelId) },
        )
    }
}
