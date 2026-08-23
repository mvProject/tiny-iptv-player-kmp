package com.mvproject.tinyiptvkmp.core.datastore

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioSerializer
import androidx.datastore.core.okio.OkioStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import okio.FileSystem
import okio.Path.Companion.toPath

object ProtoStoreFactory {
    fun <T> create(
        serializer: OkioSerializer<T>,
        scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        producePath: () -> String,
    ): ProtoStore<T> =
        ProtoStoreImpl(
            DataStoreFactory.create(
                storage =
                    OkioStorage(
                        fileSystem = FileSystem.SYSTEM,
                        serializer = serializer,
                        producePath = { producePath().toPath() },
                    ),
                scope = scope,
            )
        )
}
