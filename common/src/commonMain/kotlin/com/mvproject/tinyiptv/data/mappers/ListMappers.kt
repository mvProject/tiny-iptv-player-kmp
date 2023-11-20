/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 26.10.23, 16:03
 *
 */

package com.mvproject.tinyiptv.data.mappers

import com.mvproject.tinyiptv.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptv.data.model.epg.EpgProgram
import com.mvproject.tinyiptv.utils.TimeUtils

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