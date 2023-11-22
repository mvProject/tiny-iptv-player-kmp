/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.datasource.EpgDataSource
import com.mvproject.tinyiptvkmp.data.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.data.repository.SelectedEpgRepository
import com.mvproject.tinyiptvkmp.utils.KLog
import com.mvproject.tinyiptvkmp.utils.TimeUtils.actualDate

class UpdateEpgUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val selectedEpgRepository: SelectedEpgRepository,
    private val epgProgramRepository: EpgProgramRepository,
    private val epgDataSource: EpgDataSource,

    ) {
    suspend operator fun invoke() {
        val updateEpgIds = selectedEpgRepository.getAllSelectedEpg()
            .map { selected ->
                selected.channelEpgId
            }

        if (updateEpgIds.isNotEmpty()) {
            updateEpgIds.forEach { id ->
                val programs = epgDataSource.getRemoteEpg(channelsId = id)

                KLog.w("UpdateEpgUseCase programs channel:${id}, count:${programs.count()}")

                epgProgramRepository.insertEpgPrograms(
                    channelId = id,
                    channelEpgPrograms = programs
                )
            }
        }

        preferenceRepository.apply {
            setEpgUnplannedUpdateRequired(state = false)
            setEpgLastUpdate(timestamp = actualDate)
        }
    }
}