/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.network

import com.mvproject.tinyiptvkmp.data.model.response.ChannelsEpgInfoResponse
import com.mvproject.tinyiptvkmp.data.model.response.EpgProgramsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.request
import io.ktor.http.HttpMethod

class NetworkRepository(private val service: HttpClient) {

    suspend fun loadPlaylistData(url: String) = service.request(url) {
        method = HttpMethod.Get
    }

    suspend fun loadEpgInfo(): ChannelsEpgInfoResponse =
        service.request(EPG_IPTVX_INFO_URL) {
            method = HttpMethod.Get
        }.body()

    suspend fun getEpgProgramsForChannel(channelId: String): EpgProgramsResponse =
        service.request(
            EPG_IPTVX_CHANNEL_URL + channelId + EPG_IPTVX_CHANNEL_SUFF
        ) {
            method = HttpMethod.Get
        }.body()

    private companion object {
        const val EPG_IPTVX_INFO_URL = "https://epg.iptvx.one/api/channels.json"
        const val EPG_IPTVX_CHANNEL_URL = "https://epg.iptvx.one/api/id/"
        const val EPG_IPTVX_CHANNEL_SUFF = ".json"
    }
}

