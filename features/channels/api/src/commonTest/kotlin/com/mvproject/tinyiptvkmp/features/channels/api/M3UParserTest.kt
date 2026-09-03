package com.mvproject.tinyiptvkmp.features.channels.api

import com.mvproject.tinyiptvkmp.features.channels.api.data.parser.M3UParser
import kotlin.test.Test
import kotlin.test.assertEquals

class M3UParserTest {
    @Test
    fun parseLinesToChannelsParsesValidEntriesInSinglePassShape() {
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

        val channels =
            M3UParser.parseLinesToChannels(
                playlistId = "playlist-1",
                lines = content.lineSequence(),
            )

        assertEquals(3, channels.size)
        assertEquals("News Channel", channels[0].channelName)
        assertEquals("https://example.com/news.png", channels[0].channelLogo)
        assertEquals("NEWS", channels[0].channelGroup)
        assertEquals("https://example.com/news.m3u8", channels[0].channelUrl)
        assertEquals("playlist-1", channels[0].parentListId)
        assertEquals("Sports Channel", channels[1].channelName)
        assertEquals("LIVE SPORTS", channels[1].channelGroup)
        assertEquals("https://example.com/sports.m3u8", channels[1].channelUrl)
        assertEquals("Plain Fallback", channels[2].channelName)
        assertEquals("plain-stream-id", channels[2].channelUrl)
    }
}
