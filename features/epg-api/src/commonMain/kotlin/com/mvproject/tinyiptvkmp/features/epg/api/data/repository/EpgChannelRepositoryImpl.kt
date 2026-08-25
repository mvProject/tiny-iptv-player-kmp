/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.07.24, 16:02
 *
 */

package com.mvproject.tinyiptvkmp.features.epg.api.data.repository

import com.mvproject.tinyiptvkmp.features.epg.api.data.local.EpgChannelLocalDataSource
import com.mvproject.tinyiptvkmp.features.epg.api.data.mapper.EpgMappers.toEpgChannel
import com.mvproject.tinyiptvkmp.features.epg.api.data.mapper.EpgMappers.toEpgChannelEntity
import com.mvproject.tinyiptvkmp.features.epg.api.data.remote.EpgChannelRemoteDataSource
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgChannel
import com.mvproject.tinyiptvkmp.features.epg.api.domain.repository.EpgChannelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class EpgChannelRepositoryImpl(
    private val localDataSource: EpgChannelLocalDataSource,
    private val remoteDataSource: EpgChannelRemoteDataSource,
) : EpgChannelRepository {
    override suspend fun loadEpgInfoData(): List<EpgChannel> =
        localDataSource.loadEpgInfoData().map {
            it.toEpgChannel()
        }

    override fun loadEpgChannels(): Flow<List<EpgChannel>> =
        localDataSource.loadEpgChannels().map { epg ->
            epg.map { it.toEpgChannel() }
        }

    override suspend fun updateChannelsFromSource(sourceUrl: String) {
        val channels = remoteDataSource
            .getChannelsFromSource(sourceUrl = sourceUrl)
            .map { it.toEpgChannelEntity() }

        localDataSource.replaceChannels(channels = channels)
    }
}
