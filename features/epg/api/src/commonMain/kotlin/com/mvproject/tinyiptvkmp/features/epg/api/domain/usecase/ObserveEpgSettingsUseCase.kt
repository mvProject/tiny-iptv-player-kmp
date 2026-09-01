package com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgSettings
import com.mvproject.tinyiptvkmp.features.epg.api.domain.repository.EpgRepository
import kotlinx.coroutines.flow.Flow

interface ObserveEpgSettingsUseCase {
    operator fun invoke(): Flow<EpgSettings>
}

internal class ObserveEpgSettingsUseCaseImpl(
    private val repository: EpgRepository,
) : ObserveEpgSettingsUseCase {
    override operator fun invoke(): Flow<EpgSettings> = repository.observeEpgSettings()
}
