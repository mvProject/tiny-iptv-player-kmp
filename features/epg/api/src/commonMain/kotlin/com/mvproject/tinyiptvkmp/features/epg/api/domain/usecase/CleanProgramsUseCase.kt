package com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase

import com.mvproject.tinyiptvkmp.core.foundation.common.LONG_NO_VALUE
import com.mvproject.tinyiptvkmp.features.epg.api.domain.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.features.epg.api.domain.repository.EpgRepository
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.actualEpgDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.days

interface CleanProgramsUseCase {
    suspend operator fun invoke()
}

internal class CleanProgramsUseCaseImpl(
    private val epgRepository: EpgRepository,
    private val epgProgramRepository: EpgProgramRepository,
) : CleanProgramsUseCase {
    override suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            val current = actualEpgDate
            val settings = epgRepository.getEpgSettings()
            val lastCleanup = settings.epgProgramClean
            val lastUpdate = settings.epgDataLastUpdate
            // Check if at least one day has passed since the last cleanup
            if ((current - lastCleanup) > 1.days.inWholeMilliseconds && lastUpdate != LONG_NO_VALUE) {
                // Clean up programs with dates before the current date
                epgProgramRepository.cleanProgramsBeforeDate(date = current)
                // Update the last cleanup time
                epgRepository.markEpgProgramsCleaned(cleanedAt = current)
            }
        }
    }
}
