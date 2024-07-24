/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.07.24, 16:02
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
import com.mvproject.tinyiptvkmp.utils.TimeUtils.typeToDuration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class UpdateEpgUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val epgProgramRepository: EpgProgramRepository,
    private val epgInfoRepository: EpgInfoRepository,
    private val epgDataSource: EpgDataSource,
) {
    suspend operator fun invoke() {
        val epgUpdatePeriod = preferenceRepository.epgUpdatePeriod().first()
        val duration = typeToDuration(epgUpdatePeriod)

        val current = Clock.System.now().toEpochMilliseconds()

        val epgToUpdate =
            epgInfoRepository.loadEpgInfoData().filter { (current - it.lastUpdate) >= duration }
        KLog.e("testing UpdateEpgUseCase epgToUpdate count:${epgToUpdate.count()}")
        //  val epgIds = epgToUpdate.map { it.channelId }

        val epgIdsChunked = epgToUpdate.chunked(40)

        if (epgIdsChunked.isNotEmpty()) {
            KLog.e("testing UpdateEpgUseCase start:${current.convertTimeToReadableFormat()}")
            withContext(Dispatchers.IO) {
                epgIdsChunked.forEachIndexed { index, part ->
                    delay(500)
                    part.forEach { info ->
                        val programs = epgDataSource.getRemoteEpg(channelsId = info.channelId)
                        epgProgramRepository.insertEpgPrograms(
                            channelId = info.channelId,
                            channelEpgPrograms = programs,
                        )
                        val updatedInfo = info.copy(lastUpdate = current)
                        epgInfoRepository.updateEpgInfoUpdate(info = updatedInfo)
                    }
                }
            }
        }
        val end = Clock.System.now().toEpochMilliseconds()
        KLog.e("testing UpdateEpgUseCase end:${end.convertTimeToReadableFormat()}")

        preferenceRepository.setEpgLastUpdate(timestamp = actualDate)
    }
}
