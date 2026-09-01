/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.features.channels.api.data.parser

import com.mvproject.tinyiptvkmp.features.channels.api.data.local.PlaylistChannelParseModel

internal object M3UParser {
    private const val TAG_PLAYLIST_HEADER = "#EXTM3U"
    private const val TAG_METADATA = "#EXTINF:"
    private const val ATTR_LOGO = "tvg-logo"
    private const val ATTR_GROUP_TITLE = "group-title"
    private const val TAG_GROUP = "#EXTGRP:"

    fun parseStringToChannels(source: String): List<PlaylistChannelParseModel> =
        buildList {
            var pendingMeta: String? = null
            var pendingGroup = ""

            source.lineSequence().forEach { rawLine ->
                val line = rawLine.trim()

                when {
                    line.isEmpty() || line == TAG_PLAYLIST_HEADER -> Unit
                    line.startsWith(TAG_METADATA) -> {
                        pendingMeta = line.removePrefix(TAG_METADATA).trim()
                        pendingGroup = ""
                    }

                    line.startsWith(TAG_GROUP) && pendingMeta != null -> {
                        pendingGroup = line.substringAfter(TAG_GROUP).trim()
                    }

                    line.startsWith("#") && pendingMeta != null -> Unit

                    pendingMeta != null -> {
                        parseEntry(
                            meta = pendingMeta.orEmpty(),
                            group = pendingGroup,
                            link = line,
                        )?.let(::add)

                        pendingMeta = null
                        pendingGroup = ""
                    }
                }
            }
        }

    private fun parseEntry(
        meta: String,
        group: String,
        link: String,
    ): PlaylistChannelParseModel? {
        val logo = extractAttribute(meta, ATTR_LOGO)
        val groupTitle = extractAttribute(meta, ATTR_GROUP_TITLE)
        val actualGroup = group.ifEmpty { groupTitle }.uppercase()
        val title = meta.substringAfterLast(',').trim()

        if (title.isEmpty() || link.isEmpty()) return null

        return PlaylistChannelParseModel(link, logo, actualGroup, title)
    }

    private fun extractAttribute(meta: String, attr: String): String {
        val prefix = "$attr=\""
        val valueStart = meta.indexOf(prefix).takeIf { it >= 0 }?.plus(prefix.length) ?: return ""
        val valueEnd = meta.indexOf('"', startIndex = valueStart).takeIf { it >= 0 } ?: return ""

        return meta.substring(valueStart, valueEnd).trim()
    }
}
