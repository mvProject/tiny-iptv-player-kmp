package com.mvproject.tinyiptvkmp.core.base.mvi

import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <T> runCatchingSuspend(
    block: suspend () -> T
): Result<T> = try {
    Result.success(block())
} catch (throwable: Throwable) {
    if (throwable is CancellationException) throw throwable
    Result.failure(throwable)
}