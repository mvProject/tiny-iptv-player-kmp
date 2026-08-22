package com.mvproject.tinyiptvkmp.core.datastore.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

interface KeyValueStorage {
    fun <T : Any> observe(key: StorageKey<T>): Flow<T>

    suspend fun <T : Any> get(key: StorageKey<T>): T

    suspend fun <T : Any> set(
        key: StorageKey<T>,
        value: T,
    )

    suspend fun remove(key: StorageKey<*>)

    suspend fun clear()
}

class PreferencesKeyValueStorage(
    private val dataStore: DataStore<Preferences>,
    private val serializer: StorageSerializer = StorageSerializer(),
) : KeyValueStorage {
    override fun <T : Any> observe(key: StorageKey<T>): Flow<T> =
        dataStore.data.map { preferences ->
            preferences[serializer.preferencesKey(key)] ?: key.defaultValue
        }

    override suspend fun <T : Any> get(key: StorageKey<T>): T = observe(key).first()

    override suspend fun <T : Any> set(
        key: StorageKey<T>,
        value: T,
    ) {
        dataStore.edit { preferences ->
            preferences[serializer.preferencesKey(key)] = value
        }
    }

    override suspend fun remove(key: StorageKey<*>) {
        dataStore.edit { preferences ->
            preferences.remove(serializer.preferencesKey(key))
        }
    }

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
