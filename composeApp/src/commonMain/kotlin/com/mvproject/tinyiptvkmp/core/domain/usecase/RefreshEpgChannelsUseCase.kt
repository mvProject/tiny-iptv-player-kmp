package com.mvproject.tinyiptvkmp.core.domain.usecase

import com.mvproject.tinyiptvkmp.core.data.repository.EpgChannelRepository
import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.core.network.datasource.EpgChannelDatasource
import com.mvproject.tinyiptvkmp.utils.AppConstants
import com.mvproject.tinyiptvkmp.utils.KLog
import com.mvproject.tinyiptvkmp.utils.TimeUtils
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
            KLog.d("testing RefreshEpgChannelsUseCase isRequired $isRequired")
            if (isRequired) {
                val sourceChannels =
                    epgChannelDatasource.getChannelsFromSource(
                        sourceUrl = AppConstants.CHANNELS_SOURCE_URL,
                    )

                channelRepository.updateChannels(channels = sourceChannels)
                KLog.w("testing RefreshEpgChannelsUseCase updateChannels complete")
                preferenceRepository.apply {
                    setEpgInfoDataLastUpdate(timestamp = currentDate)
                    setChannelsEpgInfoUpdateRequired(state = true)
                }
            }
        }
    }
}