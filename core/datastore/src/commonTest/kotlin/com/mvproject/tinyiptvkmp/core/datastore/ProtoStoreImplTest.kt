package com.mvproject.tinyiptvkmp.core.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ProtoStoreImplTest {
    @Test
    fun updateTransformsDataStoreValue() =
        runTest {
            val dataStore = FakeDataStore(TestProto(count = 1))
            val store = ProtoStoreImpl(dataStore)

            store.update { current -> current.copy(count = current.count + 1) }

            assertEquals(TestProto(count = 2), store.data.first())
        }
}

private data class TestProto(
    val count: Int,
)

private class FakeDataStore<T>(
    initialValue: T,
) : DataStore<T> {
    private val values = MutableStateFlow(initialValue)

    override val data: Flow<T> = values

    override suspend fun updateData(transform: suspend (t: T) -> T): T {
        val updated = transform(values.value)
        values.value = updated
        return updated
    }
}
