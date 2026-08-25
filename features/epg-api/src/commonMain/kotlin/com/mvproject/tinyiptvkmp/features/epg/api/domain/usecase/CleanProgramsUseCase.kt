package com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase

import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import com.mvproject.tinyiptvkmp.core.datastore.preferences.AppPreferencesProto
import com.mvproject.tinyiptvkmp.features.epg.api.domain.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.actualEpgDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.days

private const val longNoValue = -1L

interface CleanProgramsUseCase {
    suspend operator fun invoke()
}

internal class CleanProgramsUseCaseImpl(
    private val preferencesStore: ProtoStore<AppPreferencesProto>,
    private val epgProgramRepository: EpgProgramRepository,
) : CleanProgramsUseCase {
    override suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            val current = actualEpgDate
            val preferences = preferencesStore.data.first()
            val lastCleanup = preferences.epgProgramClean
            val lastUpdate = preferences.epgDataLastUpdate
            // Check if at least one day has passed since the last cleanup
            if ((current - lastCleanup) > 1.days.inWholeMilliseconds && lastUpdate != longNoValue) {
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
