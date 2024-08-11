/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.datasource

import com.mvproject.tinyiptvkmp.data.mappers.ParseMappers.asProgramEntities
import com.mvproject.tinyiptvkmp.data.model.epg.EpgProgram
import com.mvproject.tinyiptvkmp.data.network.NetworkRepository
import com.mvproject.tinyiptvkmp.utils.KLog
import com.mvproject.tinyiptvkmp.utils.TimeUtils.actualDate


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
