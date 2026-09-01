package com.mvproject.tinyiptvkmp.features.player.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.player.api.domain.model.PlayerSettings
import com.mvproject.tinyiptvkmp.features.player.api.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow

interface ObservePlayerSettingsUseCase {
    operator fun invoke(): Flow<PlayerSettings>
}

internal class ObservePlayerSettingsUseCaseImpl(
    private val repository: PlayerRepository,
) : ObservePlayerSettingsUseCase {
    override operator fun invoke(): Flow<PlayerSettings> = repository.observePlayerSettings()
}
