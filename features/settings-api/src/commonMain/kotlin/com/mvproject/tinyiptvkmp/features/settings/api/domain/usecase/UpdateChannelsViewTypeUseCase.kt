package com.mvproject.tinyiptvkmp.features.settings.api.domain.usecase

import com.mvproject.tinyiptvkmp.core.foundation.model.ChannelsViewType
import com.mvproject.tinyiptvkmp.features.settings.api.domain.repository.SettingsRepository

interface UpdateChannelsViewTypeUseCase {
    suspend operator fun invoke(type: ChannelsViewType)
}

internal class UpdateChannelsViewTypeUseCaseImpl(
    private val repository: SettingsRepository,
) : UpdateChannelsViewTypeUseCase {
    override suspend operator fun invoke(type: ChannelsViewType) {
        repository.updateChannelsViewType(type)
    }
}
