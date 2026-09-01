package com.mvproject.tinyiptvkmp.features.epg.api.data.local

import com.mvproject.tinyiptvkmp.features.epg.api.data.local.database.EpgChannelDao
import com.mvproject.tinyiptvkmp.features.epg.api.data.local.database.EpgChannelEntity
import kotlinx.coroutines.flow.Flow

internal class EpgChannelLocalDataSourceImpl(
    private val epgChannelDao: EpgChannelDao,
) : EpgChannelLocalDataSource {
    override suspend fun loadEpgInfoData(): List<EpgChannelEntity> =
        epgChannelDao.getEpgInfo()

    override fun loadEpgChannels(): Flow<List<EpgChannelEntity>> =
        epgChannelDao.getEpgChannels()

    override suspend fun replaceChannels(channels: List<EpgChannelEntity>) {
        epgChannelDao.deleteEpgChannels()
        epgChannelDao.insertEpgChannels(data = channels)
    }
}
