package com.mvproject.tinyiptvkmp.infrastructure.logging

import io.ktor.client.plugins.logging.LogLevel

enum class AppLogSeverity {
    Verbose,
    Debug,
    Info,
    Warn,
    Error,
    Assert,
}

object AppLoggingConfig {
    var minimumSeverity: AppLogSeverity = AppLogSeverity.Warn
        private set

    var httpLogLevel: LogLevel = LogLevel.NONE
        private set

    var isImageDebugLoggingEnabled: Boolean = false
        private set

    fun applyDefaults(isDebug: Boolean) {
        minimumSeverity = if (isDebug) AppLogSeverity.Debug else AppLogSeverity.Warn
        httpLogLevel = if (isDebug) LogLevel.HEADERS else LogLevel.NONE
        isImageDebugLoggingEnabled = isDebug
    }
}
