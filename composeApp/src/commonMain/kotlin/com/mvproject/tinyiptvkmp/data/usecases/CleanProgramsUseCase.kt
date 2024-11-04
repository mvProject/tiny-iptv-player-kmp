package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.utils.AppConstants.LONG_NO_VALUE
import com.mvproject.tinyiptvkmp.utils.TimeUtils
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
            if ((current - lastCleanup) > 1.days.inWholeMilliseconds && lastUpdate != LONG_NO_VALUE) {
                // Clean up programs with dates before the current date
                epgProgramRepository.cleanProgramsBeforeDate(date = current)
                // Update the last cleanup time
                preferenceRepository.setProgramsCleanTime(timeInMillis = current)
            }
        }
    }
}
