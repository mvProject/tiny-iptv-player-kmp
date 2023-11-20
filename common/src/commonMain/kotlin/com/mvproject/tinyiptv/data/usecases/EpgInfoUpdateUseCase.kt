/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 03.09.23, 21:48
 *
 */

package com.mvproject.tinyiptv.data.usecases

import com.mvproject.tinyiptv.data.datasource.EpgInfoDataSource
import com.mvproject.tinyiptv.data.repository.EpgInfoRepository
import com.mvproject.tinyiptv.data.repository.PreferenceRepository
import com.mvproject.tinyiptv.utils.TimeUtils

class EpgInfoUpdateUseCase(
    private val epgInfoDataSource: EpgInfoDataSource,
    private val preferenceRepository: PreferenceRepository,
    private val epgInfoRepository: EpgInfoRepository
) {
    suspend operator fun invoke() {
        val epgInfo = epgInfoDataSource.getEpgInfo().distinctBy { it.channelNames }

        if (preferenceRepository.isEpgInfoDataExist()) {
            epgInfoRepository.updateEpgInfoData(epgInfo)
        } else {
            epgInfoRepository.addEpgInfoData(epgInfo)
            preferenceRepository.setEpgInfoDataExist(state = true)
        }

        preferenceRepository.apply {
            setEpgInfoDataLastUpdate(timestamp = TimeUtils.actualDate)
            setChannelsEpgInfoUpdateRequired(state = true)
        }

    }
}