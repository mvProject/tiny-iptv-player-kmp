package com.mvproject.tinyiptvkmp.infrastructure.logging.di

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.StaticConfig
import co.touchlab.kermit.platformLogWriter
import com.mvproject.tinyiptvkmp.infrastructure.logging.AppLogSeverity
import com.mvproject.tinyiptvkmp.infrastructure.logging.AppLogger
import com.mvproject.tinyiptvkmp.infrastructure.logging.AppLoggingConfig
import com.mvproject.tinyiptvkmp.infrastructure.logging.KermitAppLogger
import org.koin.dsl.module

private const val BASE_TAG = "com.mvproject.tinyiptvkmp"

val loggingModule =
    module {
        single {
            Logger(
                config = StaticConfig(
                    minSeverity = AppLoggingConfig.minimumSeverity.toKermitSeverity(),
                    logWriterList = listOf(platformLogWriter()),
                ),
                tag = BASE_TAG,
            )
        }

        factory<AppLogger> { params ->
            val tag = params.getOrNull<String>()
            val baseLogger = get<Logger>()
            KermitAppLogger(
                logger = if (tag != null) {
                    baseLogger.withTag(tag)
                } else {
                    baseLogger
                },
            )
        }
    }

private fun AppLogSeverity.toKermitSeverity(): Severity =
    when (this) {
        AppLogSeverity.Verbose -> Severity.Verbose
        AppLogSeverity.Debug -> Severity.Debug
        AppLogSeverity.Info -> Severity.Info
        AppLogSeverity.Warn -> Severity.Warn
        AppLogSeverity.Error -> Severity.Error
        AppLogSeverity.Assert -> Severity.Assert
    }
