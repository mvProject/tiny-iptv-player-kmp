package com.mvproject.tinyiptvkmp.data.network

import com.mvproject.tinyiptvkmp.utils.KLog
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.GZip
import kotlinx.serialization.json.Json

expect fun createPlatformHttpClient(): HttpClient

internal fun createHttpClient(): HttpClient =
    createPlatformHttpClient().config {
        install(Logging) {
            logger =
                object : Logger {
                    override fun log(message: String) {
                        KLog.w("Ktor log:") { message }
                    }
                }
            level = LogLevel.ALL
        }
        install(HttpHeaders.ContentEncoding) {
            GZip
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