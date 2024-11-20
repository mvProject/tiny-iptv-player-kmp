/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 27.10.23, 10:58
 *
 */

package com.mvproject.tinyiptvkmp.core.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import okio.Path.Companion.toPath
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val datastoreModule =
    module {
        includes(datastorePathModule())
        single<DataStore<Preferences>> {
            createDataStore(producePath = get())
        }
        singleOf(::PreferenceRepository)
    }

private fun createDataStore(
    //corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = ReplaceFileCorruptionHandler(
    //    produceNewData = { emptyPreferences() },
    //),
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
    producePath: () -> String
): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        //   corruptionHandler = corruptionHandler,
        scope = coroutineScope,
        produceFile = { producePath().toPath() },
    )
}