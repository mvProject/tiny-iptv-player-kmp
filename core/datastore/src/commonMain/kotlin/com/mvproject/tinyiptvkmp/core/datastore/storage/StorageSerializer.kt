package com.mvproject.tinyiptvkmp.core.datastore.storage

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

class StorageSerializer {
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> preferencesKey(key: StorageKey<T>): Preferences.Key<T> =
        when (key.defaultValue) {
            is String -> stringPreferencesKey(key.name)
            is Int -> intPreferencesKey(key.name)
            is Long -> longPreferencesKey(key.name)
            is Boolean -> booleanPreferencesKey(key.name)
            is Float -> floatPreferencesKey(key.name)
            is Double -> doublePreferencesKey(key.name)
            is Set<*> -> stringSetPreferencesKey(key.name)
            else -> error("Unsupported storage key type for ${key.name}: ${key.defaultValue::class}")
        } as Preferences.Key<T>
}
