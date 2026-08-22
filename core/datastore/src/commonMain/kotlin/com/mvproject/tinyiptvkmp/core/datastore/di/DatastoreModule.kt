package com.mvproject.tinyiptvkmp.core.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.mvproject.tinyiptvkmp.core.datastore.storage.KeyValueStorage
import com.mvproject.tinyiptvkmp.core.datastore.storage.PreferencesKeyValueStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import okio.Path.Companion.toPath
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module

val datastoreModule = createDatastoreModule()

fun createDatastoreModule(
    fileName: String = dataStoreFileName,
    dataStoreQualifier: Qualifier? = null,
    keyValueStorageQualifier: Qualifier? = null,
    pathProducerQualifier: Qualifier? = null,
) =
    module {
        includes(platformDatastorePathModule(fileName, pathProducerQualifier))
        single<DataStore<Preferences>>(qualifier = dataStoreQualifier) {
            createDataStore(producePath = get(qualifier = pathProducerQualifier))
        }
        single<KeyValueStorage>(qualifier = keyValueStorageQualifier) {
            PreferencesKeyValueStorage(get(qualifier = dataStoreQualifier))
        }
    }

fun createDataStore(
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
    producePath: DatastorePathProducer,
): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        scope = coroutineScope,
        produceFile = { producePath().toPath() },
    )
