/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 03.09.23, 21:42
 *
 */

package com.mvproject.tinyiptv.data.datasource

import com.mvproject.tinyiptv.data.mappers.ParseMappers.asProgramEntities
import com.mvproject.tinyiptv.data.models.epg.EpgProgram
import com.mvproject.tinyiptv.data.network.NetworkRepository
import com.mvproject.tinyiptv.utils.TimeUtils.actualDate
import io.github.aakira.napier.Napier

class EpgDataSource(
    private val networkRepository: NetworkRepository
) {
    suspend fun getRemoteEpg(channelsId: String): List<EpgProgram> {
        val programs = try {
            networkRepository.getEpgProgramsForChannel(channelId = channelsId).chPrograms
        } catch (exc: Exception) {
            Napier.e("error ${exc.localizedMessage}")
            emptyList()
        }
        val result = if (programs.isNotEmpty()) {
            val programsMapped = programs.asProgramEntities(channelId = channelsId)

            programsMapped.filter { it.start > actualDate }

        } else emptyList()

        return result
    }
}
