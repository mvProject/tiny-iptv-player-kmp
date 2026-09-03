/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.core.network.datasource

import io.ktor.client.HttpClient
import io.ktor.client.request.prepareGet
import io.ktor.client.request.request
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.HttpMethod
import io.ktor.utils.io.ByteReadChannel

class NetworkPlaylistDatasource(private val service: HttpClient) {

    suspend fun loadPlaylistData(url: String) = service.request(url) {
        method = HttpMethod.Get
    }

    suspend fun <T> streamPlaylistData(
        url: String,
        block: suspend (ByteReadChannel) -> T,
    ): T =
        service.prepareGet(url).execute { response ->
            block(response.bodyAsChannel())
        }
}
