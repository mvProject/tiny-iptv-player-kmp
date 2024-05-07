/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 06.05.24, 12:48
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.datasource.EpgInfoDataSource
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

        if (preferenceRepository.isEpgInfoDataExist()) {
            // epgInfoRepository.updateEpgInfoData(epgInfo)
            epgInfoRepository.saveEpgInfoDataRoom(epgInfo)
        } else {
            //  epgInfoRepository.addEpgInfoData(epgInfo)
            epgInfoRepository.saveEpgInfoDataRoom(epgInfo)
            preferenceRepository.setEpgInfoDataExist(state = true)
        }

        preferenceRepository.apply {
            setEpgInfoDataLastUpdate(timestamp = TimeUtils.actualDate)
            setChannelsEpgInfoUpdateRequired(state = true)
        }
    }
}
