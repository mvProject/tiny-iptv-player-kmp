/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.07.24, 16:02
 *
 */

package com.mvproject.tinyiptvkmp.data.repository

import androidx.room.Transaction
import com.mvproject.tinyiptvkmp.core.database.db.AppDatabase
import com.mvproject.tinyiptvkmp.core.network.data.response.EpgChannelResponse
import com.mvproject.tinyiptvkmp.data.mappers.Mapper.toEpgChannelEntity
import com.mvproject.tinyiptvkmp.data.mappers.Mapper.toEpgChannelModel
import com.mvproject.tinyiptvkmp.data.model.epg.EpgChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EpgChannelRepository(
    private val appDatabase: AppDatabase,
) {
    private val epgInfoDao = appDatabase.epgInfoDao()

    suspend fun loadEpgInfoData(): List<EpgChannel> =
        epgInfoDao.getEpgInfo().map {
            it.toEpgChannelModel()
        }

    fun loadEpgChannels(): Flow<List<EpgChannel>> =
        epgInfoDao.getEpgChannels().map { epg ->
            epg.map { it.toEpgChannelModel() }
        }

    @Transaction
    suspend fun updateChannels(channels: List<EpgChannelResponse>) {
        epgInfoDao.apply {
            deleteEpgChannels()
            insertEpgChannels(data = channels.map { it.toEpgChannelEntity() })
        }
    }
}
