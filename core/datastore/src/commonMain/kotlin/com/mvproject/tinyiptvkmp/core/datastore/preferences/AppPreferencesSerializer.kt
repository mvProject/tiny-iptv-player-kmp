package com.mvproject.tinyiptvkmp.core.datastore.preferences

import androidx.datastore.core.okio.OkioSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import okio.BufferedSink
import okio.BufferedSource

@OptIn(ExperimentalSerializationApi::class)
internal object AppPreferencesSerializer : OkioSerializer<AppPreferencesProto> {
    override val defaultValue = AppPreferencesProto()

    override suspend fun readFrom(source: BufferedSource): AppPreferencesProto =
        try {
            ProtoBuf.decodeFromByteArray(source.readByteArray())
        } catch (_: SerializationException) {
            defaultValue
        }

    override suspend fun writeTo(
        t: AppPreferencesProto,
        sink: BufferedSink,
    ) {
        sink.write(ProtoBuf.encodeToByteArray(t))
    }
}
