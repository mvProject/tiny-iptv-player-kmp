package com.mvproject.tinyiptvkmp.core.datastore.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module

actual fun platformDatastorePathModule(
    fileName: String,
    pathProducerQualifier: Qualifier?,
): Module =
    module {
        single<DatastorePathProducer>(qualifier = pathProducerQualifier) {
            { androidContext().filesDir.resolve(fileName).absolutePath }
        }
    }
