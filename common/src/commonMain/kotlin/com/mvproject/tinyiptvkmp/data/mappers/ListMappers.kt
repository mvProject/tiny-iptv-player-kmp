/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.mappers

import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.data.model.epg.EpgProgram
import com.mvproject.tinyiptvkmp.utils.TimeUtils

object ListMappers {
    fun List<EpgProgram>.toActual(): List<EpgProgram> {
        return this.filter { it.stop > TimeUtils.actualDate }
    }

    fun List<TvPlaylistChannel>.withRefreshedEpg(): List<TvPlaylistChannel> {
        return this.map {
            val epg = it.channelEpg.toActual()
            it.copy(channelEpg = epg)
        }
    }
}