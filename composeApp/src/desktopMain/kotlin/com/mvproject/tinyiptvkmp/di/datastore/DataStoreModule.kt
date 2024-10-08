/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.07.24, 14:43
 *
 */

package com.mvproject.tinyiptvkmp.di.datastore

import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import com.mvproject.tinyiptvkmp.platform.dataStoreFileName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okio.Path.Companion.toPath
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformDataStoreModule(): Module =
    module {
        single {
            val path = System.getProperty("java.io.tmpdir")
            PreferenceDataStoreFactory.createWithPath(
                corruptionHandler =
                    ReplaceFileCorruptionHandler(
                        produceNewData = { emptyPreferences() },
                    ),
                scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
                produceFile = {
                    "$path/$dataStoreFileName".toPath()
                },
            )
        }
    }
