package com.mvproject.tinyiptvkmp.features.channels.api

import com.mvproject.tinyiptvkmp.features.channels.api.data.parser.M3UParser
import kotlin.test.Test
import kotlin.test.assertEquals

class M3UParserTest {
    @Test
    fun parseStringToChannelsParsesValidEntriesInSinglePassShape() {
        val content =
            """
            #EXTM3U
            #EXTINF:-1 tvg-logo="https://example.com/news.png" group-title="News",News Channel
            https://example.com/news.m3u8
            #EXTINF:-1 group-title="Sports",Sports Channel
            #EXTGRP: Live Sports
            #EXTVLCOPT:http-user-agent=TinyIptv
            https://example.com/sports.m3u8
            #EXTINF:-1 tvg-logo="https://example.com/empty.png",
            https://example.com/invalid.m3u8
            #EXTINF:-1,Plain Fallback
            plain-stream-id
            """.trimIndent()

        val channels = M3UParser.parseStringToChannels(content)

        assertEquals(3, channels.size)
        assertEquals("News Channel", channels[0].channel)
        assertEquals("https://example.com/news.png", channels[0].logoURL)
        assertEquals("NEWS", channels[0].groupTitle)
        assertEquals("https://example.com/news.m3u8", channels[0].streamURL)
        assertEquals("Sports Channel", channels[1].channel)
        assertEquals("LIVE SPORTS", channels[1].groupTitle)
        assertEquals("https://example.com/sports.m3u8", channels[1].streamURL)
        assertEquals("Plain Fallback", channels[2].channel)
        assertEquals("plain-stream-id", channels[2].streamURL)
    }
}
