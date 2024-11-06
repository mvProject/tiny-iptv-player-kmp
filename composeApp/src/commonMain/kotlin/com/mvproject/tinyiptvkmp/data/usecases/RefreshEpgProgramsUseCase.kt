package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.datasource.EpgProgramDatasource
import com.mvproject.tinyiptvkmp.data.model.response.EpgProgramResponse
import com.mvproject.tinyiptvkmp.data.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.utils.AppConstants.PROGRAMS_SOURCE_URL
import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.utils.KLog
import com.mvproject.tinyiptvkmp.utils.TimeUtils
import com.mvproject.tinyiptvkmp.utils.TimeUtils.parseToInstant
import com.mvproject.tinyiptvkmp.utils.TimeUtils.typeToDuration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes

class RefreshEpgProgramsUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val epgProgramRepository: EpgProgramRepository,
    private val epgProgramDatasource: EpgProgramDatasource,
) {
    suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            val currentDate = TimeUtils.actualDate
            val lastUpdate = preferenceRepository.lastEpgUpdate()
            val periodUpdate = typeToDuration(preferenceRepository.getMainEpgUpdatePeriod())
            val lastUpdateElapsed = currentDate - lastUpdate
            val isRequired = lastUpdateElapsed > periodUpdate
            KLog.d("testing RefreshEpgProgramsUseCase isRequired $isRequired")
            if (isRequired) {
                delay(1.minutes)
                var programmeCount = 0
                val programsDto = mutableListOf<EpgProgramResponse>()
                var currentId = String.empty

                epgProgramDatasource.downloadAndParseXml(
                    url = PROGRAMS_SOURCE_URL,
                    onProgrammeParsed = { programme ->
                        val start = parseToInstant(programme.start)
                        val end = parseToInstant(programme.stop)
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
                                    programsDto.add(epgProgramResponse)
                                }
                            }
                        }
                        if (programmeCount > 0 && programmeCount % 10000 == 0) {
                            KLog.i("testing Parsed $programmeCount programmes")
                        }
                    },
                )
                KLog.w("testing Parsed Complete $programmeCount programmes")
                if (programmeCount != 0) {
                    preferenceRepository.setEpgLastUpdate(timestamp = currentDate)
                }
            }
        }
    }
}
