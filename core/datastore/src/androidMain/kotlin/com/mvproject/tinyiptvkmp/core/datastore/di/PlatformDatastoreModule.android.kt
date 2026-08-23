package com.mvproject.tinyiptvkmp.core.datastore.di

import com.mvproject.tinyiptvkmp.core.datastore.DataStorePathProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal actual val platformDatastoreModule =
    module {
        single { DataStorePathProvider(androidContext()) }
    }
