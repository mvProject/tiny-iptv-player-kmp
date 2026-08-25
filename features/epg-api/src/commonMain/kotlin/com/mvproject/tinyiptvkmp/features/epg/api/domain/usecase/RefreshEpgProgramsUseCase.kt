package com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase

import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import com.mvproject.tinyiptvkmp.core.datastore.preferences.AppPreferencesProto
import com.mvproject.tinyiptvkmp.features.epg.api.domain.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.actualEpgDate
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.epgUpdatePeriodToDuration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes

private const val programsSourceUrl = "http://epg.one/epg2.xml.gz"

interface RefreshEpgProgramsUseCase {
    suspend operator fun invoke(onRefreshState: (RefreshState) -> Unit)
}

internal class RefreshEpgProgramsUseCaseImpl(
    private val preferencesStore: ProtoStore<AppPreferencesProto>,
    private val epgProgramRepository: EpgProgramRepository,
) : RefreshEpgProgramsUseCase {
    override suspend operator fun invoke(onRefreshState: (RefreshState) -> Unit) {
        withContext(Dispatchers.IO) {
            val currentDate = actualEpgDate
            val preferences = preferencesStore.data.first()
            val lastUpdate = preferences.epgDataLastUpdate
            val periodUpdate = epgUpdatePeriodToDuration(preferences.epgMainLastUpdatePeriod)
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
                    preferencesStore.update { current ->
                        current.copy(epgDataLastUpdate = currentDate)
                    }
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
