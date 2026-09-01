package com.mvproject.tinyiptvkmp.features.epg.api.data.remote

import com.mvproject.tinyiptvkmp.core.network.data.response.EpgChannelResponse

internal interface EpgChannelRemoteDataSource {
    suspend fun getChannelsFromSource(sourceUrl: String): List<EpgChannelResponse>
}
