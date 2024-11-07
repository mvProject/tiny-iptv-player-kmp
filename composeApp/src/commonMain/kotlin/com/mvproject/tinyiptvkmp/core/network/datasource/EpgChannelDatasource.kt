package com.mvproject.tinyiptvkmp.core.network.datasource

import com.mvproject.tinyiptvkmp.core.data.utils.ParserUtils.loadElements
import com.mvproject.tinyiptvkmp.core.data.utils.ParserUtils.parseElementDataAsChannel
import com.mvproject.tinyiptvkmp.core.network.data.response.EpgChannelResponse

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
