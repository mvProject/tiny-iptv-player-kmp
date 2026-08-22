package com.mvproject.tinyiptvkmp.infrastructure.logging

import co.touchlab.kermit.Logger

internal class KermitAppLogger(
    private val logger: Logger,
) : AppLogger {

    override val tag: String = logger.tag

    override fun v(throwable: Throwable?, message: () -> String) {
        logger.v(throwable = throwable, message = message)
    }

    override fun d(throwable: Throwable?, message: () -> String) {
        logger.d(throwable = throwable, message = message)
    }

    override fun i(throwable: Throwable?, message: () -> String) {
        logger.i(throwable = throwable, message = message)
    }

    override fun w(throwable: Throwable?, message: () -> String) {
        logger.w(throwable = throwable, message = message)
    }

    override fun e(throwable: Throwable?, message: () -> String) {
        logger.e(throwable = throwable, message = message)
    }
}
