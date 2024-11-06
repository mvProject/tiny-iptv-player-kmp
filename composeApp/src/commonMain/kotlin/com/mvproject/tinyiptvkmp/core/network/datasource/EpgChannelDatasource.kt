package com.mvproject.tinyiptvkmp.core.network.datasource

import com.mvproject.tinyiptvkmp.core.network.data.response.EpgChannelResponse
import com.mvproject.tinyiptvkmp.utils.ParserUtils.loadElements
import com.mvproject.tinyiptvkmp.utils.ParserUtils.parseElementDataAsChannel

class EpgChannelDatasource {
    suspend fun getChannelsFromSource(sourceUrl: String): List<EpgChannelResponse> {
        val parsedTable = loadElements(sourceUrl = sourceUrl)

        val networkChannels =
            buildList {
                parsedTable.forEach { element ->
                    val (id, logo, names) = element.parseElementDataAsChannel()

                    names.forEach { name ->
                        add(
                            EpgChannelResponse(
                                channelName = name,
                                channelId = id,
                                channelIcon = logo,
                            ),
                        )
                    }
                }
            }

        return networkChannels
    }
}
