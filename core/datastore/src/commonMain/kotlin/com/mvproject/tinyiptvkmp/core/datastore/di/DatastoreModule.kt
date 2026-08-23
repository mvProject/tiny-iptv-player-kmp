package com.mvproject.tinyiptvkmp.core.datastore.di

import org.koin.dsl.module

val datastoreModule =
    module {
        includes(platformDatastoreModule)
    }
