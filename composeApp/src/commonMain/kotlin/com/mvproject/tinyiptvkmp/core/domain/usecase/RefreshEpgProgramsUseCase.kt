package com.mvproject.tinyiptvkmp.core.domain.usecase

import com.mvproject.tinyiptvkmp.core.common.AppConstants
import com.mvproject.tinyiptvkmp.core.common.AppConstants.INT_VALUE_1
import com.mvproject.tinyiptvkmp.core.common.AppConstants.INT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.common.utils.TimeUtils
import com.mvproject.tinyiptvkmp.core.data.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.core.network.data.response.EpgProgramResponse
import com.mvproject.tinyiptvkmp.core.network.datasource.EpgProgramDatasource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes

class RefreshEpgProgramsUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val epgProgramRepository: EpgProgramRepository,
    private val epgProgramDatasource: EpgProgramDatasource,
) {
    suspend operator fun invoke(onRefreshState: (RefreshState) -> Unit) {
        withContext(Dispatchers.IO) {
            val currentDate = TimeUtils.actualDate
            val lastUpdate = preferenceRepository.lastEpgUpdate()
            val periodUpdate =
                TimeUtils.typeToDuration(preferenceRepository.getMainEpgUpdatePeriod())
            val lastUpdateElapsed = currentDate - lastUpdate
            val isRequired = lastUpdateElapsed > periodUpdate

            if (isRequired) {
                delay(INT_VALUE_1.minutes)

                var programmeCount = INT_VALUE_ZERO
                val programsDto = mutableListOf<EpgProgramResponse>()
                var currentId = String.empty

                onRefreshState(RefreshState.Started)

                epgProgramDatasource.downloadAndParseXml(
                    url = AppConstants.PROGRAMS_SOURCE_URL,
                    onProgrammeParsed = { programme ->
                        val start = TimeUtils.parseToInstant(programme.start)
                        val end = TimeUtils.parseToInstant(programme.stop)
                        if (end >= currentDate) {
                            programmeCount++
                            val epgProgramResponse =
                                EpgProgramResponse(
                                    dateTimeStart = start,
                                    dateTimeEnd = end,
                                    title = programme.title,
                                    description = programme.desc ?: String.empty,
                                )

                            if (currentId.isBlank()) {
                                currentId = programme.channel
                                onRefreshState(RefreshState.Update)
                                programsDto.add(epgProgramResponse)
                            } else {
                                if (programme.channel == currentId) {
                                    programsDto.add(epgProgramResponse)
                                } else {
                                    if (programsDto.isNotEmpty()) {
                                        epgProgramRepository.updatePrograms(
                                            channelId = currentId,
                                            programs = programsDto,
                                        )
                                    }
                                    programsDto.clear()
                                    currentId = programme.channel
                                    onRefreshState(RefreshState.Update)
                                    programsDto.add(epgProgramResponse)
                                }
                            }
                        }
                    },
                )

                if (programmeCount != INT_VALUE_ZERO) {
                    preferenceRepository.setEpgLastUpdate(timestamp = currentDate)
                }
                onRefreshState(RefreshState.Ended)
            }
        }
    }
}

sealed interface RefreshState {
    data object Update : RefreshState
    data object Started : RefreshState
    data object Ended : RefreshState
}