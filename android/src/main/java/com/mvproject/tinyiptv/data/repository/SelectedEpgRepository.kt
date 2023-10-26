/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.data.repository

import com.mvproject.tinyiptv.TinyIptvDatabase
import com.mvproject.tinyiptv.data.mappers.EntityMapper.toSelectedEpg
import com.mvproject.tinyiptv.data.model.epg.SelectedEpg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tinyiptvdb.SelectedEpgEntity

class SelectedEpgRepository(private val db: TinyIptvDatabase) {
    private val selectedEpgQueries = db.selectedEpgEntityQueries

    suspend fun addSelectedEpg(epg: SelectedEpg) {
        withContext(Dispatchers.IO) {
            selectedEpgQueries.addSelectedEpgEntity(
                SelectedEpgEntity(
                    channelEpgId = epg.channelEpgId,
                    channelName = epg.channelName,
                )
            )
        }
    }

    suspend fun deleteSelectedEpg(id: String) {
        withContext(Dispatchers.IO) {
            selectedEpgQueries.deleteSelectedEpgEntity(
                id = id
            )
        }
    }

    fun getAllSelectedEpg(): List<SelectedEpg> {
        return selectedEpgQueries.getSelectedEpgEntities()
            .executeAsList()
            .map { entity ->
                entity.toSelectedEpg()
            }
    }

    fun getSelectedEpg(channel: String): SelectedEpg {
        return selectedEpgQueries.getSelectedEpgEntity(name = channel)
            .executeAsOne()
            .toSelectedEpg()
    }
}

