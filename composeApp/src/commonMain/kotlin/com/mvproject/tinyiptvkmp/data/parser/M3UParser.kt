/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.parser

import com.mvproject.tinyiptvkmp.data.model.parse.PlaylistChannelParseModel
import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty

object M3UParser {
    private const val TAG_PLAYLIST_HEADER = "#EXTM3U"
    private const val TAG_METADATA = "#EXTINF:"
    private const val ATTR_LOGO = "tvg-logo"
    private const val ATTR_GROUP_TITLE = "group-title"
    private const val TAG_GROUP = "#EXTGRP"

    fun parsePlaylist(string: String): List<PlaylistChannelParseModel> {
        val lines = string.split(TAG_METADATA.toRegex()).toTypedArray()
        return buildList {
            for (_line in lines) {
                if (!_line.contains(TAG_PLAYLIST_HEADER)) {
                    // meta + url
                    val entry = _line.split("\n".toRegex()).toTypedArray()
                    if (entry.size > 1) {
                        var meta = entry[0]
                        var link = String.empty
                        var group = String.empty
                        entry.forEach { content ->
                            if (content.contains("http") || content.contains("https")) {
                                link = content.trim()
                            }
                            if (content.contains(TAG_GROUP)) {
                                group = content.split(":").last().trim()
                            }
                        }
                        meta = meta.trim()

                        val logo =
                            if (meta.contains(ATTR_LOGO)) {
                                val start = meta.indexOf(ATTR_LOGO) + ATTR_LOGO.length + 2
                                val end = meta.substring(start)
                                end.substring(0, end.indexOf("\"")).trim()
                            } else {
                                String.empty
                            }

                        val groupTitle =
                            if (meta.contains(ATTR_GROUP_TITLE)) {
                                val start = meta.indexOf(ATTR_GROUP_TITLE) + ATTR_GROUP_TITLE.length + 2
                                val end = meta.substring(start)
                                end.substring(0, end.indexOf("\"")).trim()
                            } else {
                                String.empty
                            }

                        val actualGroup = group.ifEmpty { groupTitle }.uppercase()
                        val actualLink = link.ifEmpty { entry[1].trim() }

                        val title = meta.substring(meta.indexOfLast { it == ',' } + 1)
                        val m3u = PlaylistChannelParseModel(actualLink, logo, actualGroup, title)
                        add(m3u)
                    } else {
                        add(
                            PlaylistChannelParseModel(
                                entry[0].trim(),
                                String.empty,
                                String.empty,
                                String.empty,
                            ),
                        )
                    }
                }
            }
        }
    }

    fun parsePlaylist2(string: String): List<PlaylistChannelParseModel> {
        return string.split(TAG_METADATA)
            .filter { !it.contains(TAG_PLAYLIST_HEADER) }
            .mapNotNull { parseEntry(it) }
    }

    private fun parseEntry(entry: String): PlaylistChannelParseModel? {
        val lines = entry.split("\n")
        if (lines.size < 2) return null

        val meta = lines[0].trim()
        val link = lines.find { it.startsWith("http") || it.startsWith("https") }?.trim() ?: lines[1].trim()
        val group = lines.find { it.startsWith(TAG_GROUP) }?.substringAfter(TAG_GROUP)?.trim() ?: ""

        val logo = extractAttribute(meta, ATTR_LOGO)
        val groupTitle = extractAttribute(meta, ATTR_GROUP_TITLE)
        val actualGroup = group.ifEmpty { groupTitle }.uppercase()
        val title = meta.substringAfterLast(',').trim()

        return PlaylistChannelParseModel(link, logo, actualGroup, title)
    }

    private fun extractAttribute(meta: String, attr: String): String {
        val regex = """$attr="([^"]*)"""".toRegex()
        return regex.find(meta)?.groupValues?.get(1) ?: ""
    }
}

