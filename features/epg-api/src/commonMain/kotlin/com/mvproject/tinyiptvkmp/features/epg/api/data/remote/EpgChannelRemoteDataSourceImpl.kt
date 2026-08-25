package com.mvproject.tinyiptvkmp.features.epg.api.data.remote

import com.mvproject.tinyiptvkmp.core.network.data.response.EpgChannelResponse
import com.mvproject.tinyiptvkmp.core.network.datasource.EpgChannelDatasource

internal class EpgChannelRemoteDataSourceImpl(
    private val epgChannelDatasource: EpgChannelDatasource,
) : EpgChannelRemoteDataSource {
    override suspend fun getChannelsFromSource(sourceUrl: String): List<EpgChannelResponse> =
        epgChannelDatasource.getChannelsFromSource(sourceUrl = sourceUrl)
}
