package com.mvproject.tinyiptvkmp.core.datastore

import androidx.datastore.core.DataStore

internal class ProtoStoreImpl<T>(
    private val dataStore: DataStore<T>,
) : ProtoStore<T> {
    override val data = dataStore.data

    override suspend fun update(transform: (T) -> T) {
        dataStore.updateData { current -> transform(current) }
    }
}
