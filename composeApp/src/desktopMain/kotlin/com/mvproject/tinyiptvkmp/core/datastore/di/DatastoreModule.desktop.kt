/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.07.24, 14:43
 *
 */

package com.mvproject.tinyiptvkmp.core.datastore.di

import com.mvproject.tinyiptvkmp.core.datastore.client.createDataStore
import com.mvproject.tinyiptvkmp.core.datastore.client.dataStoreFileName
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun datastoreModule(): Module =
    module {
        single {
            val tempDir = System.getProperty("java.io.tmpdir")
            val path = "$tempDir/$dataStoreFileName"
            createDataStore(producePath = { path })
        }
    }
