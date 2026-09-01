package com.mvproject.tinyiptvkmp.persistence.epg.room

import com.mvproject.tinyiptvkmp.features.epg.api.data.datasource.local.EpgChannelLocalDataSource
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgChannel
import com.mvproject.tinyiptvkmp.persistence.epg.room.database.EpgChannelDao
import com.mvproject.tinyiptvkmp.persistence.epg.room.mapper.toEpgChannel
import com.mvproject.tinyiptvkmp.persistence.epg.room.mapper.toEpgChannelEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class RoomEpgChannelLocalDataSource(
    private val epgChannelDao: EpgChannelDao,
) : EpgChannelLocalDataSource {
    override suspend fun loadEpgInfoData(): List<EpgChannel> =
        epgChannelDao.getEpgInfo().map { it.toEpgChannel() }

    override fun loadEpgChannels(): Flow<List<EpgChannel>> =
        epgChannelDao.getEpgChannels().map { channels -> channels.map { it.toEpgChannel() } }

    override suspend fun replaceChannels(channels: List<EpgChannel>) {
        epgChannelDao.deleteEpgChannels()
        epgChannelDao.insertEpgChannels(data = channels.map { it.toEpgChannelEntity() })
    }
}


