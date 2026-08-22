package com.mvproject.tinyiptvkmp.infrastructure.logging

interface AppLogger {
    val tag: String

    fun v(throwable: Throwable? = null, message: () -> String)

    fun d(throwable: Throwable? = null, message: () -> String)

    fun i(throwable: Throwable? = null, message: () -> String)

    fun w(throwable: Throwable? = null, message: () -> String)

    fun e(throwable: Throwable? = null, message: () -> String)
}
