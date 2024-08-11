/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 17.06.24, 11:13
 *
 */

package com.mvproject.tinyiptvkmp.data.datasource

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.select.Elements
import com.mvproject.tinyiptvkmp.data.model.response.EpgInfoResponse
import com.mvproject.tinyiptvkmp.data.network.NetworkRepository
import com.mvproject.tinyiptvkmp.utils.KLog

class EpgInfoDataSource(
    private val networkRepository: NetworkRepository,
) {
    suspend fun getEpgInfo(): List<EpgInfoResponse> {
        /*        val infoResult = networkRepository.loadEpgInfo().channels

                val filtered =
                    infoResult.filter { it.channelId.isNotEmpty() && it.channelIcon.isNotEmpty() }

                return buildList {
                    filtered.forEach { chn ->
                        if (chn.channelNames.contains(CHANNEL_NAME_SPLIT_DELIMITER)) {
                            val splitNames = chn.channelNames.split(CHANNEL_NAME_SPLIT_DELIMITER)
                            splitNames.forEach { spl ->
                                add(
                                    EpgInfoResponse(
                                        channelId = chn.channelId,
                                        channelIcon = chn.channelIcon,
                                        channelNames = spl
                                    )
                                )
                            }
                        } else add(chn)
                    }
                }*/

        val doc: Document =
            Ksoup.parseGetRequest(
                url = "https://epg.ott-play.com/php/show_prow.php?f=iptvx.one/epg.xml.gz",
            )

        val table: Elements = doc.select("tr")
        val parsed = table.drop(1)

        val channels =
            buildList {
                parsed.forEach { element ->
                    val selected = element.select("td")
                    val id = selected[2].text()
                    val logo = selected[0].select("img").attr("src")
                    val names = selected[1].textNodes()

                    names.forEach { name ->
                        add(
                            EpgInfoResponse(
                                channelNames = name.text(),
                                channelId = id,
                                channelIcon = logo,
                            ),
                        )
                    }
                }
            }

        KLog.w("testing channels count ${channels.count()}")
        return channels
    }

    private companion object {
        const val CHANNEL_NAME_SPLIT_DELIMITER = " • "
    }
}
