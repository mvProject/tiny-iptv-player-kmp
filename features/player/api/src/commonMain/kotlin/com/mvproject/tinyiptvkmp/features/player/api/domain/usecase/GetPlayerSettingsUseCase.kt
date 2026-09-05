package com.mvproject.tinyiptvkmp.features.player.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.player.api.domain.model.PlayerSettings
import com.mvproject.tinyiptvkmp.features.player.api.domain.repository.PlayerRepository

interface GetPlayerSettingsUseCase {
    suspend operator fun invoke(): PlayerSettings
}

internal class GetPlayerSettingsUseCaseImpl(
    private val repository: PlayerRepository,
) : GetPlayerSettingsUseCase {
    override suspend operator fun invoke(): PlayerSettings = repository.getPlayerSettings()
}
