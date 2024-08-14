/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 17.05.24, 18:16
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.datasource.EpgInfoDataSource
import com.mvproject.tinyiptvkmp.data.mappers.ParseMappers.toEpgInfo
import com.mvproject.tinyiptvkmp.data.repository.EpgInfoRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.utils.TimeUtils

class EpgInfoUpdateUseCase(
    private val epgInfoDataSource: EpgInfoDataSource,
    private val preferenceRepository: PreferenceRepository,
    private val epgInfoRepository: EpgInfoRepository,
) {
    suspend operator fun invoke() {
        val epgInfo = epgInfoDataSource.getEpgInfo().distinctBy { it.channelNames }

        val epgData = epgInfo.map { it.toEpgInfo() }

        epgInfoRepository.saveEpgInfoData(info = epgData)

        preferenceRepository.apply {
            setEpgInfoDataLastUpdate(timestamp = TimeUtils.actualDate)
            setEpgInfoDataExist(state = true)
            setChannelsEpgInfoUpdateRequired(state = true)
        }
    }
}
