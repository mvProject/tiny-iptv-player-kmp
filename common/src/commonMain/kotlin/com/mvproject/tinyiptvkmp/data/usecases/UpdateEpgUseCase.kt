/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 09:44
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.datasource.EpgDataSource
import com.mvproject.tinyiptvkmp.data.repository.EpgInfoRepository
import com.mvproject.tinyiptvkmp.data.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.data.repository.SelectedEpgRepository
import com.mvproject.tinyiptvkmp.utils.KLog
import com.mvproject.tinyiptvkmp.utils.TimeUtils.actualDate
import com.mvproject.tinyiptvkmp.utils.TimeUtils.convertTimeToReadableFormat
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock

class UpdateEpgUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val selectedEpgRepository: SelectedEpgRepository,
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
            epgIdsChunked.forEachIndexed { index, part ->
                KLog.d("testing UpdateEpgUseCase index:$index")
                delay(500)
                part.forEach { id ->
                    val programs = epgDataSource.getRemoteEpg(channelsId = id)
                    KLog.w("testing UpdateEpgUseCase id:$id")
                    epgProgramRepository.insertEpgPrograms(
                        channelId = id,
                        channelEpgPrograms = programs,
                    )
                }
            }

            // epgIdsChunked.first().forEach { id ->
            //     val programs = epgDataSource.getRemoteEpg(channelsId = id)
//
            //     KLog.e("testing UpdateEpgUseCase programs channel:$id, count ${programs.count()}")

            //       //  epgProgramRepository.insertEpgPrograms(
            //       //      channelId = id,
            //       //      channelEpgPrograms = programs,
            //       //  )
            //  }
        }
        val end = Clock.System.now().toEpochMilliseconds()
        KLog.w("testing UpdateEpgUseCase start:${start.convertTimeToReadableFormat()}, end:${end.convertTimeToReadableFormat()}")
        // val updateEpgIds =
        //    selectedEpgRepository.getAllSelectedEpg()
        //        .map { selected ->
        //            selected.channelEpgId
        //        }

        // if (updateEpgIds.isNotEmpty()) {
        //    updateEpgIds.forEach { id ->
        //        val programs = epgDataSource.getRemoteEpg(channelsId = id)

        //        KLog.w("UpdateEpgUseCase programs channel:$id, count:${programs.count()}")

        //        epgProgramRepository.insertEpgPrograms(
        //            channelId = id,
        //            channelEpgPrograms = programs,
        //        )
        //    }
        // }

        preferenceRepository.apply {
            //    setEpgUnplannedUpdateRequired(state = false)
            setEpgLastUpdate(timestamp = actualDate)
        }
    }
}
