/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.network

import io.ktor.client.HttpClient
import io.ktor.client.request.request
import io.ktor.http.HttpMethod

class NetworkRepository(private val service: HttpClient) {

    suspend fun loadPlaylistData(url: String) = service.request(url) {
        method = HttpMethod.Get
    }
}

