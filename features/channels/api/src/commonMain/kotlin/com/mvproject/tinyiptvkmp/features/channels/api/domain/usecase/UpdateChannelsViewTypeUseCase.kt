package com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase

import com.mvproject.tinyiptvkmp.core.foundation.model.ChannelsViewType
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelsRepository

interface UpdateChannelsViewTypeUseCase {
    suspend operator fun invoke(type: ChannelsViewType)
}

internal class UpdateChannelsViewTypeUseCaseImpl(
    private val repository: ChannelsRepository,
) : UpdateChannelsViewTypeUseCase {
    override suspend operator fun invoke(type: ChannelsViewType) {
        repository.updateChannelsViewType(type)
    }
}
