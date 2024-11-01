/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 06.05.24, 11:36
 *
 */

package com.mvproject.tinyiptvkmp.data.mappers

import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.data.model.epg.EpgProgram
import com.mvproject.tinyiptvkmp.utils.TimeUtils

object ListMappers {
    fun List<EpgProgram>.toActual(): List<EpgProgram> = this.filter { it.dateTimeEnd > TimeUtils.actualDate }

    fun List<TvPlaylistChannel>.withRefreshedEpg(): List<TvPlaylistChannel> =
        this.map {
            val epg = it.programs.toActual()
            it.copy(programs = epg)
        }
}
