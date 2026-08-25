package com.mvproject.tinyiptvkmp.features.channels.api

import com.mvproject.tinyiptvkmp.features.channels.api.data.parser.M3UParser
import kotlin.test.Test
import kotlin.test.assertEquals

class M3UParserTest {
    @Test
    fun parseStringToChannels_readsChannelMetadata() {
        val source = """
            #EXTM3U
            #EXTINF:-1 tvg-logo="https://example.com/logo.png" group-title="News", Example News
            https://example.com/news.m3u8
        """.trimIndent()

        val channels = M3UParser.parseStringToChannels(source = source)

        assertEquals(1, channels.size)
        assertEquals("Example News", channels.first().channel)
        assertEquals("https://example.com/logo.png", channels.first().logoURL)
        assertEquals("NEWS", channels.first().groupTitle)
        assertEquals("https://example.com/news.m3u8", channels.first().streamURL)
    }
}
