package com.mvproject.tinyiptvkmp.core.network.datasource

import com.mvproject.tinyiptvkmp.core.network.data.parse.ProgramParsed
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import io.ktor.client.HttpClient
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.contentLength
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okio.GzipSource
import okio.buffer
import org.koin.core.component.KoinComponent
import okio.use as okiouse

class EpgProgramDatasource(
    private val client: HttpClient,
) : KoinComponent {
    private val logger by injectLogger()

    suspend fun downloadAndParseXml(
        url: String,
        onProgrammeParsed: suspend (ProgramParsed) -> Unit,
    ) {
        try {
            client.prepareGet(url).execute { response ->
                logger.i { "testing File download started. Content length: ${response.contentLength()}" }
                val channel = response.bodyAsChannel()
                parseGzippedXml(channel, onProgrammeParsed)
            }
        } catch (ex: Exception) {
            logger.e(ex) { "testing Error downloading or parsing XML: ${ex.message}" }
        }
    }

    private suspend fun parseGzippedXml(
        channel: ByteReadChannel,
        onProgrammeParsed: suspend (ProgramParsed) -> Unit,
    ) = withContext(Dispatchers.Default) {
        logger.i { "testing start parsing programmes" }

        val buffer = ByteArray(8192) // 8KB buffer
        val gzipSource = GzipSource(object : okio.Source {
            override fun read(sink: okio.Buffer, byteCount: Long): Long {
                return runBlocking {
                    val bytesRead = channel.readAvailable(
                        buffer,
                        0,
                        buffer.size.coerceAtMost(byteCount.toInt())
                    )
                    if (bytesRead > 0) {
                        sink.write(buffer, 0, bytesRead)
                    }
                    if (bytesRead == -1) -1 else bytesRead.toLong()
                }
            }

            override fun timeout() = okio.Timeout.NONE
            override fun close() {}
        })

        gzipSource.buffer().okiouse { bufferedSource ->
            var currentProgram: ProgramParsed? = null
            var currentElement = ""
            val currentContent = StringBuilder()

            while (true) {
                val line = bufferedSource.readUtf8Line() ?: break

                when {
                    line.contains("<programme") -> {
                        currentProgram =
                            ProgramParsed(
                                start = extractAttribute(line, "start"),
                                stop = extractAttribute(line, "stop"),
                                channel = extractAttribute(line, "channel"),
                            )
                    }

                    line.contains("<title") -> {
                        currentElement = "title"
                        currentContent.clear()
                        if (line.contains("</title>")) {
                            currentProgram?.title = extractContent(line)
                            currentElement = ""
                        }
                    }

                    line.contains("<desc") -> {
                        currentElement = "desc"
                        currentContent.clear()
                        if (line.contains("</desc>")) {
                            currentProgram?.desc = extractContent(line)
                            currentElement = ""
                        }
                    }

                    line.contains("</title>") -> {
                        currentContent.append(extractContent(line))
                        currentProgram?.title = currentContent.toString().trim()
                        currentElement = ""
                    }

                    line.contains("</desc>") -> {
                        currentContent.append(extractContent(line))
                        currentProgram?.desc = currentContent.toString().trim()
                        currentElement = ""
                    }

                    currentElement.isNotEmpty() -> {
                        currentContent.append(line.trim()).append(" ")
                    }

                    line.contains("</programme>") -> {
                        currentProgram?.let { onProgrammeParsed(it) }
                        currentProgram = null
                    }
                }
            }
        }
    }

    private fun extractAttribute(
        line: String,
        attribute: String,
    ): String {
        val regex = "$attribute=\"([^\"]*)\"".toRegex()
        return regex.find(line)?.groupValues?.get(1) ?: ""
    }

    private fun extractContent(line: String): String {
        val startTag = line.indexOf(">")
        val endTag = line.lastIndexOf("<")
        return if (startTag != -1 && endTag != -1 && startTag < endTag) {
            line.substring(startTag + 1, endTag)
        } else {
            ""
        }
    }
}
