package com.mvproject.tinyiptvkmp.core.domain.usecase

import co.touchlab.kermit.Logger
import com.mvproject.tinyiptvkmp.core.common.AppConstants
import com.mvproject.tinyiptvkmp.core.common.utils.TimeUtils
import com.mvproject.tinyiptvkmp.core.data.repository.EpgChannelRepository
import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.core.network.datasource.EpgChannelDatasource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RefreshEpgChannelsUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val epgChannelDatasource: EpgChannelDatasource,
    private val channelRepository: EpgChannelRepository,
) {
    suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            val currentDate = TimeUtils.actualDate
            val lastUpdate = preferenceRepository.getEpgInfoDataLastUpdate()
            val updatePeriod =
                TimeUtils.typeToDuration(preferenceRepository.getEpgInfoUpdatePeriod())
            val isRequired = (currentDate - lastUpdate) > updatePeriod
            Logger.d("testing RefreshEpgChannelsUseCase isRequired $isRequired")
            if (isRequired) {
                val sourceChannels =
                    epgChannelDatasource.getChannelsFromSource(
                        sourceUrl = AppConstants.CHANNELS_SOURCE_URL,
                    )

                channelRepository.updateChannels(channels = sourceChannels)
                Logger.w("testing RefreshEpgChannelsUseCase updateChannels complete")
                preferenceRepository.apply {
                    setEpgInfoDataLastUpdate(timestamp = currentDate)
                    setChannelsEpgInfoUpdateRequired(state = true)
                }
            }
        }
    }
}