package com.mvproject.tinyiptvkmp.features.channels.api.data.local

import androidx.datastore.core.okio.OkioSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import okio.BufferedSink
import okio.BufferedSource

@OptIn(ExperimentalSerializationApi::class)
internal object ChannelsPreferencesSerializer : OkioSerializer<ChannelsPreferencesProto> {
    override val defaultValue = ChannelsPreferencesProto()

    override suspend fun readFrom(source: BufferedSource): ChannelsPreferencesProto =
        try {
            ProtoBuf.decodeFromByteArray(source.readByteArray())
        } catch (_: SerializationException) {
            defaultValue
        }

    override suspend fun writeTo(
        t: ChannelsPreferencesProto,
        sink: BufferedSink,
    ) {
        sink.write(ProtoBuf.encodeToByteArray(t))
    }
}
