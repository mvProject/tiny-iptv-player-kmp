package com.mvproject.tinyiptvkmp.features.player.api.data.local

import androidx.datastore.core.okio.OkioSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import okio.BufferedSink
import okio.BufferedSource

@OptIn(ExperimentalSerializationApi::class)
internal object PlayerPreferencesSerializer : OkioSerializer<PlayerPreferencesProto> {
    override val defaultValue = PlayerPreferencesProto()

    override suspend fun readFrom(source: BufferedSource): PlayerPreferencesProto =
        try {
            ProtoBuf.decodeFromByteArray(source.readByteArray())
        } catch (_: SerializationException) {
            defaultValue
        }

    override suspend fun writeTo(
        t: PlayerPreferencesProto,
        sink: BufferedSink,
    ) {
        sink.write(ProtoBuf.encodeToByteArray(t))
    }
}
