package com.mvproject.tinyiptvkmp.core.datastore

import kotlinx.coroutines.flow.Flow

interface ProtoStore<T> {
    val data: Flow<T>

    suspend fun update(transform: (T) -> T)
}
