package com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.remote

import com.mvproject.tinyiptvkmp.core.network.datasource.NetworkPlaylistDatasource
import com.mvproject.tinyiptvkmp.features.channels.api.data.parser.M3UParser.parseLinesToChannels
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okio.Buffer
import okio.Source
import okio.Timeout
import okio.buffer
import okio.use as okioUse

internal class PlaylistChannelRemoteDataSourceImpl(
    private val network: NetworkPlaylistDatasource,
) : PlaylistChannelRemoteDataSource {
    override suspend fun loadPlaylistContent(
        playlistId: String,
        url: String,
    ): List<PlaylistChannel> {
        return network.streamPlaylistData(url = url) { channel ->
            withContext(Dispatchers.Default) {
                channel.asSource().buffer().okioUse { source ->
                    parseLinesToChannels(
                        playlistId = playlistId,
                        lines = generateSequence { source.readUtf8Line() },
                    )
                }
            }
        }
    }

    private fun ByteReadChannel.asSource(): Source =
        object : Source {
            private val readBuffer = ByteArray(DEFAULT_BUFFER_SIZE)

            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead =
                    runBlocking {
                        readAvailable(
                            readBuffer,
                            0,
                            readBuffer.size.coerceAtMost(byteCount.toInt()),
                        )
                    }

                if (bytesRead > 0) {
                    sink.write(readBuffer, 0, bytesRead)
                }

                return if (bytesRead == -1) -1 else bytesRead.toLong()
            }

            override fun timeout(): Timeout = Timeout.NONE

            override fun close() = Unit
        }

    private companion object {
        const val DEFAULT_BUFFER_SIZE = 8192
    }
}
