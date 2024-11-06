/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.core.datastore.di

import com.mvproject.tinyiptvkmp.core.datastore.client.createDataStore
import com.mvproject.tinyiptvkmp.core.datastore.client.dataStoreFileName
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun datastoreModule(): Module =
    module {
        single {
            val path = androidContext().filesDir.resolve(dataStoreFileName).absolutePath
            createDataStore(producePath = { path })
        }
    }
