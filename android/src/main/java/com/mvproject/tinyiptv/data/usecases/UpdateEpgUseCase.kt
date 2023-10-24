/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 03.09.23, 21:48
 *
 */

package com.mvproject.tinyiptv.data.usecases

import com.mvproject.tinyiptv.data.datasource.EpgDataSource
import com.mvproject.tinyiptv.data.repository.EpgProgramRepository
import com.mvproject.tinyiptv.data.repository.PreferenceRepository
import com.mvproject.tinyiptv.data.repository.SelectedEpgRepository
import com.mvproject.tinyiptv.utils.TimeUtils.actualDate
import io.github.aakira.napier.Napier

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

                Napier.w("testing UpdateEpgUseCase programs channel:${id}, count:${programs.count()}")

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