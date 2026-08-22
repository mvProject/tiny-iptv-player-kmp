package com.mvproject.tinyiptvkmp.core.network.client

import com.mvproject.tinyiptvkmp.infrastructure.logging.ktorLogger
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

expect fun createPlatformHttpClient(): HttpClient

fun createHttpClient(): HttpClient =
    createPlatformHttpClient().config {
        install(Logging) {
            logger = ktorLogger()
            level = LogLevel.ALL
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
