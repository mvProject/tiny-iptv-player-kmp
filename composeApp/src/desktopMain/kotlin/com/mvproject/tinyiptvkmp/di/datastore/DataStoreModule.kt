/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.07.24, 14:43
 *
 */

package com.mvproject.tinyiptvkmp.di.datastore

import com.mvproject.tinyiptvkmp.data.datastore.createDataStore
import com.mvproject.tinyiptvkmp.data.datastore.dataStoreFileName
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformDataStoreModule(): Module =
    module {
        single {
            val tempDir = System.getProperty("java.io.tmpdir")
            val path = "$tempDir/$dataStoreFileName"
            createDataStore(producePath = { path })
        }
    }
