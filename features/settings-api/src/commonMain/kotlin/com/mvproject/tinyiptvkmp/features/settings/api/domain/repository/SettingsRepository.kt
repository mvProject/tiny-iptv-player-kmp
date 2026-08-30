package com.mvproject.tinyiptvkmp.features.settings.api.domain.repository

import com.mvproject.tinyiptvkmp.core.foundation.model.ChannelsViewType
import com.mvproject.tinyiptvkmp.features.settings.api.domain.model.GeneralSettings
import com.mvproject.tinyiptvkmp.features.settings.api.domain.model.PlayerSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observeGeneralSettings(): Flow<GeneralSettings>

    fun observePlayerSettings(): Flow<PlayerSettings>

    suspend fun updateInfoUpdatePeriod(period: Int)

    suspend fun updateEpgUpdatePeriod(period: Int)

    suspend fun updateChannelsViewType(type: ChannelsViewType)

    suspend fun updateFullscreenMode(enabled: Boolean)

    suspend fun updateVideoSize(mode: Int)
}
