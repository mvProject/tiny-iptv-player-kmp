package com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.ChannelsSettings
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelsRepository
import kotlinx.coroutines.flow.Flow

interface ObserveChannelsSettingsUseCase {
    operator fun invoke(): Flow<ChannelsSettings>
}

internal class ObserveChannelsSettingsUseCaseImpl(
    private val repository: ChannelsRepository,
) : ObserveChannelsSettingsUseCase {
    override operator fun invoke(): Flow<ChannelsSettings> =
        repository.observeChannelsSettings()
}
