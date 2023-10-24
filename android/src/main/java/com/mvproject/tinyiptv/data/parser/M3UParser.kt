/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.data.parser

import com.mvproject.tinyiptv.data.models.parse.PlaylistChannelParseModel
import com.mvproject.tinyiptv.utils.AppConstants.EMPTY_STRING

object M3UParser {
    private const val TAG_PLAYLIST_HEADER = "#EXTM3U"
    private const val TAG_METADATA = "#EXTINF:"
    private const val TAG_PLAYLIST_NAME = "#PLAYLIST"
    private const val ATTR_LOGO = "tvg-logo"
    private const val ATTR_GROUP_TITLE = "group-title"
    private const val ATTR_ID = "tvg-id"
    private const val TAG_GROUP = "#EXTGRP"


    //#EXTM3U
    //#EXTINF:-1 tvg-logo="https://i.imgur.com/62H9un9.png" group-title="+ IPTV Playlist",+ IPTV Playlist
    //https://10.10.12.15:8080/asdasd

    //EXTM3U
    //#EXTINF:0 tvg-rec="3",VF Счастливы Вместе
    //#EXTGRP:кино
    //http://8quelnjh.rostelekom.xyz/iptv/M6GBE2U7CEXCZG/9242/index.m3u8


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

    //#EXTINF:0 group-title="спорт" tvg-id="6509" tvg-logo="http://epg.it999.ru/img/6509.png" tvg-rec="0",ESPN HD BR
    //http://localhost/iptv/00000000000000/19056/index.m3u8

   /* fun parseInfo(infoString: String): List<ChannelsInfoParseModel> {
        val lines = infoString.split(TAG_METADATA.toRegex()).toTypedArray()
        return buildList {
            for (_line in lines) {
                if (!_line.contains(TAG_PLAYLIST_HEADER)) {
                    val entry = _line.split("\n".toRegex()).toTypedArray()
                    if (entry.size > 1) {
                        val meta = entry[0].trim()

                        val id = if (meta.contains(ATTR_ID)) {
                            val start = meta.indexOf(ATTR_ID) + ATTR_ID.length + 2
                            val end = meta.substring(start)
                            end.substring(0, end.indexOf("\"")).trim()
                        } else EMPTY_STRING


                        val logo = if (meta.contains(ATTR_LOGO)) {
                            val start = meta.indexOf(ATTR_LOGO) + ATTR_LOGO.length + 2
                            val end = meta.substring(start)
                            end.substring(0, end.indexOf("\"")).trim()

                        } else EMPTY_STRING

                        val title = meta.substring(meta.indexOfLast { it == ',' } + 1)

                        add(
                            ChannelsInfoParseModel(
                                title = title,
                                id = id,
                                logo = logo
                            )
                        )
                    }
                }
            }
        }
    }*/
}