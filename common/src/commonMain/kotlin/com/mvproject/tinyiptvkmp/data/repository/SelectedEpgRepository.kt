/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.repository

import com.mvproject.tinyiptvkmp.TinyIptvKmpDatabase
import com.mvproject.tinyiptvkmp.data.mappers.EntityMapper.toSelectedEpg
import com.mvproject.tinyiptvkmp.data.model.epg.SelectedEpg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tinyiptvkmpdb.SelectedEpgEntity

class SelectedEpgRepository(private val db: TinyIptvKmpDatabase) {
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

