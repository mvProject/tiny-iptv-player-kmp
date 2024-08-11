/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.parser

import com.mvproject.tinyiptvkmp.data.model.parse.PlaylistChannelParseModel
import com.mvproject.tinyiptvkmp.utils.AppConstants.EMPTY_STRING

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
                    //meta + url
                    val entry = _line.split("\n".toRegex()).toTypedArray()
                    if (entry.size > 1) {
                        var meta = entry[0]
                        var link = EMPTY_STRING
                        var group = EMPTY_STRING
                        entry.forEach { content ->
                            if (content.contains("http") || content.contains("https")) {
                                link = content.trim()
                            }
                            if (content.contains(TAG_GROUP)) {
                                group = content.split(":").last().trim()
                            }
                        }
                        meta = meta.trim()

                        val logo = if (meta.contains(ATTR_LOGO)) {
                            val start = meta.indexOf(ATTR_LOGO) + ATTR_LOGO.length + 2
                            val end = meta.substring(start)
                            end.substring(0, end.indexOf("\"")).trim()

                        } else EMPTY_STRING

                        val groupTitle = if (meta.contains(ATTR_GROUP_TITLE)) {
                            val start = meta.indexOf(ATTR_GROUP_TITLE) + ATTR_GROUP_TITLE.length + 2
                            val end = meta.substring(start)
                            end.substring(0, end.indexOf("\"")).trim()
                        } else EMPTY_STRING

                        val actualGroup = group.ifEmpty { groupTitle }.uppercase()
                        val actualLink = link.ifEmpty { entry[1].trim() }

                        val title = meta.substring(meta.indexOfLast { it == ',' } + 1)
                        val m3u = PlaylistChannelParseModel(actualLink, logo, actualGroup, title)
                        add(m3u)

                    } else {
                        add(
                            PlaylistChannelParseModel(
                                entry[0].trim(),
                                EMPTY_STRING,
                                EMPTY_STRING,
                                EMPTY_STRING
                            )
                        )
                    }
                }
            }
        }
    }
}