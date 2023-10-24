/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.data.network

import com.mvproject.tinyiptv.data.models.response.ChannelsEpgInfoResponse
import com.mvproject.tinyiptv.data.models.response.EpgProgramsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.request
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.HttpMethod
import io.ktor.http.Url
import io.ktor.utils.io.ByteReadChannel

class NetworkRepository(private val service: HttpClient) {

    suspend fun loadPlaylistData(url: String) = service.request(url) {
        method = HttpMethod.Get
    }

    suspend fun loadEpgData(): ByteReadChannel {
        val response = service.request(Url(EPG_IT_999_URL)) {
            method = HttpMethod.Get
        }
        return response.bodyAsChannel()
    }

    suspend fun loadMainInfo() = service.request(EPG_IT_999_INFO_URL) {
        method = HttpMethod.Get
    }.bodyAsChannel()

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
        const val EPG_IT_999_URL = "http://epg.it999.ru/epg.xml.gz"
        const val EPG_IT_999_INFO_URL = "http://epg.it999.ru/edem_epg_ico3.m3u8"
        const val EPG_IPTVX_INFO_URL = "https://epg.iptvx.one/api/channels.json"
        const val EPG_IPTVX_CHANNEL_URL = "https://epg.iptvx.one/api/id/"
        const val EPG_IPTVX_CHANNEL_SUFF = ".json"
    }
}

