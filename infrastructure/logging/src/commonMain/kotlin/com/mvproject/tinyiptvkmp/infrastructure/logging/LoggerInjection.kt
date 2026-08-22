package com.mvproject.tinyiptvkmp.infrastructure.logging

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

fun KoinComponent.injectLogger(): Lazy<AppLogger> =
    inject { parametersOf(this::class.simpleName) }

fun KoinComponent.injectLogger(tag: String): Lazy<AppLogger> =
    inject { parametersOf(tag) }
