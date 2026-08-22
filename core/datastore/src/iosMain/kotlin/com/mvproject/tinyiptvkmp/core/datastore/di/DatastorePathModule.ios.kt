package com.mvproject.tinyiptvkmp.core.datastore.di

import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module
import platform.Foundation.NSHomeDirectory

actual fun platformDatastorePathModule(
    fileName: String,
    pathProducerQualifier: Qualifier?,
): Module =
    module {
        single<DatastorePathProducer>(qualifier = pathProducerQualifier) {
            { datastorePath(fileName) }
        }
    }

private fun datastorePath(fileName: String): String = "${NSHomeDirectory()}/Documents/$fileName"
