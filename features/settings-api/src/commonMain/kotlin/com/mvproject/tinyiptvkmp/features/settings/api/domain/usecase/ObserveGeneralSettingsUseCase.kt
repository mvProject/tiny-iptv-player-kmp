package com.mvproject.tinyiptvkmp.features.settings.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.settings.api.domain.model.GeneralSettings
import com.mvproject.tinyiptvkmp.features.settings.api.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

interface ObserveGeneralSettingsUseCase {
    operator fun invoke(): Flow<GeneralSettings>
}

internal class ObserveGeneralSettingsUseCaseImpl(
    private val repository: SettingsRepository,
) : ObserveGeneralSettingsUseCase {
    override operator fun invoke(): Flow<GeneralSettings> = repository.observeGeneralSettings()
}
