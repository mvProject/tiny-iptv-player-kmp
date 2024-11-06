package com.mvproject.tinyiptvkmp.core.network.datasource

import com.mvproject.tinyiptvkmp.core.network.data.parse.ProgramParsed
import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.utils.KLog
import io.ktor.client.HttpClient
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.contentLength
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.GzipSource
import okio.buffer
import okio.source
import okio.use

class EpgProgramDatasource(
    private val client: HttpClient,
) {
    suspend fun downloadAndParseXml(
        url: String,
        onProgrammeParsed: suspend (ProgramParsed) -> Unit,
    ) {
        try {
            client.use { service ->
                service.prepareGet(url).execute { response ->
                    KLog.i("testing File download started. Content length: ${response.contentLength()}")
                    val channel = response.bodyAsChannel()
                    parseGzippedXml(channel, onProgrammeParsed)
                }

              //  val response: HttpResponse = service.get(url)
              //  KLog.i("testing File download started. Content length: ${response.contentLength()}")
              //  val channel = response.bodyAsChannel()
              //  parseGzippedXml(channel, onProgrammeParsed)
            }
        } catch (ex: Exception) {
            client.close()
            KLog.e("testing Error downloading or parsing XML: ${ex.message}")
        }
    }

    private suspend fun parseGzippedXml(
        channel: ByteReadChannel,
        onProgrammeParsed: suspend (ProgramParsed) -> Unit,
    ) = withContext(Dispatchers.Default) {
        KLog.i("testing start parsing programmes")
        val inputStream = channel.toInputStream()
        GzipSource(inputStream.source()).buffer().use { bufferedSource ->
            var currentProgram: ProgramParsed? = null
            var currentElement = String.empty
            var line: String?
            val currentContent = StringBuilder()

            while (bufferedSource.readUtf8Line().also { line = it } != null) {
                when {
                    line!!.contains("<programme") -> {
                        currentProgram =
                            ProgramParsed(
                                start = extractAttribute(line!!, "start"),
                                stop = extractAttribute(line!!, "stop"),
                                channel = extractAttribute(line!!, "channel"),
                            )
                    }

                    line!!.contains("<title") -> {
                        currentElement = "title"
                        currentContent.clear()
                        if (line!!.contains("</title>")) {
                            currentProgram?.title = extractContent(line!!)
                            currentElement = String.empty
                        }
                    }

                    line!!.contains("<desc") -> {
                        currentElement = "desc"
                        currentContent.clear()
                        if (line!!.contains("</desc>")) {
                            currentProgram?.desc = extractContent(line!!)
                            currentElement = String.empty
                        }
                    }

                    line!!.contains("</title>") -> {
                        currentContent.append(extractContent(line!!))
                        currentProgram?.title = currentContent.toString().trim()
                        currentElement = String.empty
                    }

                    line!!.contains("</desc>") -> {
                        currentContent.append(extractContent(line!!))
                        currentProgram?.desc = currentContent.toString().trim()
                        currentElement = String.empty
                    }

                    currentElement.isNotEmpty() -> {
                        currentContent.append(line!!.trim()).append(" ")
                    }

                    line!!.contains("</programme>") -> {
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
        return regex.find(line)?.groupValues?.get(1) ?: String.empty
    }

    private fun extractContent(line: String): String {
        val startTag = line.indexOf(">")
        val endTag = line.lastIndexOf("<")
        return if (startTag != -1 && endTag != -1 && startTag < endTag) {
            line.substring(startTag + 1, endTag)
        } else {
            String.empty
        }
    }
}
