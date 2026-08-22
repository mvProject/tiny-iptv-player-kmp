package com.mvproject.tinyiptvkmp.infrastructure.logging

import io.ktor.client.plugins.logging.Logger
import org.koin.core.component.KoinComponent

fun ktorLogger(tag: String = "HttpClient"): Logger =
    KtorAppLogger(KtorLoggerHolder.logger(tag))

private object KtorLoggerHolder : KoinComponent {
    fun logger(tag: String): AppLogger {
        val logger by injectLogger(tag)
        return logger
    }
}

private class KtorAppLogger(
    private val logger: AppLogger,
) : Logger {
    override fun log(message: String) {
        logger.d { message }
    }
}
