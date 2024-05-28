/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 28.05.24, 15:43
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.datasource.EpgDataSource
import com.mvproject.tinyiptvkmp.data.repository.EpgInfoRepository
import com.mvproject.tinyiptvkmp.data.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.utils.KLog
import com.mvproject.tinyiptvkmp.utils.TimeUtils.actualDate
import com.mvproject.tinyiptvkmp.utils.TimeUtils.convertTimeToReadableFormat
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock

class UpdateEpgUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val epgProgramRepository: EpgProgramRepository,
    private val epgInfoRepository: EpgInfoRepository,
    private val epgDataSource: EpgDataSource,
) {
    suspend operator fun invoke() {
        val epgIds =
            epgInfoRepository.loadEpgInfoData().map {
                it.channelId
            }

        val epgIdsChunked = epgIds.chunked(40)

        val start = Clock.System.now().toEpochMilliseconds()
        if (epgIdsChunked.isNotEmpty()) {
            KLog.e("testing UpdateEpgUseCase start:${start.convertTimeToReadableFormat()}")
            epgIdsChunked.forEachIndexed { index, part ->
                delay(500)
                part.forEach { id ->
                    val programs = epgDataSource.getRemoteEpg(channelsId = id)
                    epgProgramRepository.insertEpgPrograms(
                        channelId = id,
                        channelEpgPrograms = programs,
                    )
                }
            }
        }
        val end = Clock.System.now().toEpochMilliseconds()
        KLog.e("testing UpdateEpgUseCase end:${end.convertTimeToReadableFormat()}")

        preferenceRepository.setEpgLastUpdate(timestamp = actualDate)
    }
}
