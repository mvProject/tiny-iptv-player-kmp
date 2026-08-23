package com.mvproject.tinyiptvkmp.core.domain.usecase

import com.mvproject.tinyiptvkmp.core.common.LONG_NO_VALUE
import com.mvproject.tinyiptvkmp.core.common.utils.actualDate
import com.mvproject.tinyiptvkmp.core.data.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import com.mvproject.tinyiptvkmp.core.datastore.preferences.AppPreferencesProto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.days

class CleanProgramsUseCase(
    private val preferencesStore: ProtoStore<AppPreferencesProto>,
    private val epgProgramRepository: EpgProgramRepository,
) {
    suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            val current = actualDate
            val preferences = preferencesStore.data.first()
            val lastCleanup = preferences.epgProgramClean
            val lastUpdate = preferences.epgDataLastUpdate
            // Check if at least one day has passed since the last cleanup
            if ((current - lastCleanup) > 1.days.inWholeMilliseconds && lastUpdate != LONG_NO_VALUE) {
                // Clean up programs with dates before the current date
                epgProgramRepository.cleanProgramsBeforeDate(date = current)
                // Update the last cleanup time
                preferencesStore.update { currentPreferences ->
                    currentPreferences.copy(epgProgramClean = current)
                }
            }
        }
    }
}
