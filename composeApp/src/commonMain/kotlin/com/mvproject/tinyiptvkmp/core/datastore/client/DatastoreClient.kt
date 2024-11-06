package com.mvproject.tinyiptvkmp.core.datastore.client

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okio.Path.Companion.toPath

internal const val dataStoreFileName = "tiny_iptv.preferences_pb"

internal fun createDataStore(
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = ReplaceFileCorruptionHandler(
        produceNewData = { emptyPreferences() },
    ),
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
    producePath: () -> String
): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        corruptionHandler = corruptionHandler,
        scope = coroutineScope,
        produceFile = { producePath().toPath() },
    )
}