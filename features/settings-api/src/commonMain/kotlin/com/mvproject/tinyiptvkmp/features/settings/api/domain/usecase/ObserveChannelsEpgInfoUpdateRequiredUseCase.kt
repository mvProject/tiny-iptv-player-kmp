package com.mvproject.tinyiptvkmp.features.settings.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.settings.api.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

interface ObserveChannelsEpgInfoUpdateRequiredUseCase {
    operator fun invoke(): Flow<Boolean>
}

internal class ObserveChannelsEpgInfoUpdateRequiredUseCaseImpl(
    private val repository: SettingsRepository,
) : ObserveChannelsEpgInfoUpdateRequiredUseCase {
    override operator fun invoke(): Flow<Boolean> =
        repository.observeChannelsEpgInfoUpdateRequired()
}
