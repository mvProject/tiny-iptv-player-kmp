package com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.epg.api.domain.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.features.epg.api.domain.repository.EpgRepository
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.actualEpgDate
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.epgUpdatePeriodToDuration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes

private const val programsSourceUrl = "http://epg.one/epg2.xml.gz"

interface RefreshEpgProgramsUseCase {
    suspend operator fun invoke(onRefreshState: (RefreshState) -> Unit)
}

internal class RefreshEpgProgramsUseCaseImpl(
    private val epgRepository: EpgRepository,
    private val epgProgramRepository: EpgProgramRepository,
) : RefreshEpgProgramsUseCase {
    override suspend operator fun invoke(onRefreshState: (RefreshState) -> Unit) {
        withContext(Dispatchers.IO) {
            val currentDate = actualEpgDate
            val settings = epgRepository.getEpgSettings()
            val lastUpdate = settings.epgDataLastUpdate
            val periodUpdate = epgUpdatePeriodToDuration(settings.epgUpdatePeriod)
            val lastUpdateElapsed = currentDate - lastUpdate
            val isRequired = lastUpdateElapsed > periodUpdate

            if (isRequired) {
                delay(1.minutes)

                onRefreshState(RefreshState.Started)

                val programmeCount =
                    epgProgramRepository.updateProgramsFromSource(
                        sourceUrl = programsSourceUrl,
                        currentDate = currentDate,
                    ) {
                        onRefreshState(RefreshState.Update)
                    }

                if (programmeCount != 0) {
                    epgRepository.markEpgProgramsRefreshed(lastUpdate = currentDate)
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
