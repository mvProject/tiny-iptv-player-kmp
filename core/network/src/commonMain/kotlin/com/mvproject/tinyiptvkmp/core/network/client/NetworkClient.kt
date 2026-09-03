package com.mvproject.tinyiptvkmp.core.network.client

import com.mvproject.tinyiptvkmp.infrastructure.logging.AppLoggingConfig
import com.mvproject.tinyiptvkmp.infrastructure.logging.ktorLogger
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

expect fun createPlatformHttpClient(): HttpClient

fun createHttpClient(): HttpClient =
    createPlatformHttpClient().config {
        install(Logging) {
            logger = ktorLogger()
            level = AppLoggingConfig.httpLogLevel
            sanitizeHeader { header ->
                header.equals(HttpHeaders.Authorization, ignoreCase = true) ||
                        header.equals(HttpHeaders.Cookie, ignoreCase = true) ||
                        header.equals(HttpHeaders.SetCookie, ignoreCase = true)
            }
        }

        install(ContentEncoding) {
            gzip()
        }

        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                },
            )
        }

        install(HttpTimeout)
    }
