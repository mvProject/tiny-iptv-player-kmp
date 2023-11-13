/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 03.09.23, 21:42
 *
 */

package com.mvproject.tinyiptv.data.datasource

import com.mvproject.tinyiptv.data.mappers.ParseMappers.asProgramEntities
import com.mvproject.tinyiptv.data.model.epg.EpgProgram
import com.mvproject.tinyiptv.data.network.NetworkRepository
import com.mvproject.tinyiptv.utils.KLog
import com.mvproject.tinyiptv.utils.TimeUtils.actualDate

class EpgDataSource(
    private val networkRepository: NetworkRepository
) {
    suspend fun getRemoteEpg(channelsId: String): List<EpgProgram> {
        val programs = try {
            networkRepository.getEpgProgramsForChannel(channelId = channelsId).chPrograms
        } catch (exc: Exception) {
            KLog.e("error ${exc.localizedMessage}")
            emptyList()
        }
        val result = if (programs.isNotEmpty()) {
            val programsMapped = programs.asProgramEntities(channelId = channelsId)

            programsMapped.filter { it.start > actualDate }

        } else emptyList()

        return result
    }
}
