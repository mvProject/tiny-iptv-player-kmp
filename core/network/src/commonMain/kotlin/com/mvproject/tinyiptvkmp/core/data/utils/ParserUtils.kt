package com.mvproject.tinyiptvkmp.core.data.utils

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Element
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import org.koin.core.component.KoinComponent

object ParserUtils : KoinComponent {
    private val logger by injectLogger()

    private const val TABLE_TAG = "tr"
    private const val TABLE_ROW_TAG = "td"
    private const val IMAGE_QUERY_TAG = "img"
    private const val IMAGE_SRC_TAG = "src"

    suspend fun loadElements(sourceUrl: String): List<Element> {
        logger.d { "Loading elements from source" }
        val parsedDocument: Document = Ksoup.parseGetRequest(url = sourceUrl)
        val parsedTable = parsedDocument.select(TABLE_TAG).drop(1)

        logger.d { "Loaded table rows count=${parsedTable.count()}" }

        return parsedTable
    }

    fun Element.parseElementDataAsChannel(): Triple<String, String, List<String>> {
        val selected = this.select(TABLE_ROW_TAG)
        val id = selected[2].text()
        val logo = selected[0].select(IMAGE_QUERY_TAG).attr(IMAGE_SRC_TAG)
        val names = selected[1].textNodes().map { it.text() }

        return Triple(id, logo, names)
    }
}
