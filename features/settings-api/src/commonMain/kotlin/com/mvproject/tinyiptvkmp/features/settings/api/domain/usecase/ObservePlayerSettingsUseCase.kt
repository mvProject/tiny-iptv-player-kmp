package com.mvproject.tinyiptvkmp.features.settings.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.settings.api.domain.model.PlayerSettings
import com.mvproject.tinyiptvkmp.features.settings.api.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

interface ObservePlayerSettingsUseCase {
    operator fun invoke(): Flow<PlayerSettings>
}

internal class ObservePlayerSettingsUseCaseImpl(
    private val repository: SettingsRepository,
) : ObservePlayerSettingsUseCase {
    override operator fun invoke(): Flow<PlayerSettings> = repository.observePlayerSettings()
}
