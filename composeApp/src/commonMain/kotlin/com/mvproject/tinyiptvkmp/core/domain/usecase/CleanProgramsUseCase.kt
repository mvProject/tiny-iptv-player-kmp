package com.mvproject.tinyiptvkmp.core.domain.usecase

import com.mvproject.tinyiptvkmp.core.common.AppConstants
import com.mvproject.tinyiptvkmp.core.common.utils.TimeUtils
import com.mvproject.tinyiptvkmp.core.data.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.days

class CleanProgramsUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val epgProgramRepository: EpgProgramRepository,
) {
    suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            val current = TimeUtils.actualDate
            val lastCleanup = preferenceRepository.getProgramsCleanTime()
            val lastUpdate = preferenceRepository.lastEpgUpdate()
            // Check if at least one day has passed since the last cleanup
            if ((current - lastCleanup) > 1.days.inWholeMilliseconds && lastUpdate != AppConstants.LONG_NO_VALUE) {
                // Clean up programs with dates before the current date
                epgProgramRepository.cleanProgramsBeforeDate(date = current)
                // Update the last cleanup time
                preferenceRepository.setProgramsCleanTime(timeInMillis = current)
            }
        }
    }
}