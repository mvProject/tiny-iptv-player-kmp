/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.07.24, 16:02
 *
 */

package com.mvproject.tinyiptvkmp.features.epg.api.data.repository

import com.mvproject.tinyiptvkmp.features.epg.api.data.datasource.local.EpgChannelLocalDataSource
import com.mvproject.tinyiptvkmp.features.epg.api.data.datasource.remote.EpgChannelRemoteDataSource
import com.mvproject.tinyiptvkmp.features.epg.api.data.mapper.toEpgChannel
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgChannel
import com.mvproject.tinyiptvkmp.features.epg.api.domain.repository.EpgChannelRepository
import kotlinx.coroutines.flow.Flow

internal class EpgChannelRepositoryImpl(
    private val localDataSource: EpgChannelLocalDataSource,
    private val remoteDataSource: EpgChannelRemoteDataSource,
) : EpgChannelRepository {
    override suspend fun loadEpgInfoData(): List<EpgChannel> =
        localDataSource.loadEpgInfoData()

    override fun loadEpgChannels(): Flow<List<EpgChannel>> =
        localDataSource.loadEpgChannels()

    override suspend fun updateChannelsFromSource(sourceUrl: String) {
        val channels = remoteDataSource
            .getChannelsFromSource(sourceUrl = sourceUrl)
            .map { it.toEpgChannel() }

        localDataSource.replaceChannels(channels = channels)
    }
}
