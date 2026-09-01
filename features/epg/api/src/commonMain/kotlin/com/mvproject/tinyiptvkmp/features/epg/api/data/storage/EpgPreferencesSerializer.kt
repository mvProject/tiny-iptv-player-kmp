package com.mvproject.tinyiptvkmp.features.epg.api.data.storage

import androidx.datastore.core.okio.OkioSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import okio.BufferedSink
import okio.BufferedSource

@OptIn(ExperimentalSerializationApi::class)
internal object EpgPreferencesSerializer : OkioSerializer<EpgPreferencesProto> {
    override val defaultValue = EpgPreferencesProto()

    override suspend fun readFrom(source: BufferedSource): EpgPreferencesProto =
        try {
            ProtoBuf.decodeFromByteArray(source.readByteArray())
        } catch (_: SerializationException) {
            defaultValue
        }

    override suspend fun writeTo(
        t: EpgPreferencesProto,
        sink: BufferedSink,
    ) {
        sink.write(ProtoBuf.encodeToByteArray(t))
    }
}
