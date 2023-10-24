/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 03.09.23, 21:42
 *
 */

package com.mvproject.tinyiptv.data.datasource

import com.mvproject.tinyiptv.data.models.response.EpgInfoResponse
import com.mvproject.tinyiptv.data.network.NetworkRepository

class EpgInfoDataSource(
    private val networkRepository: NetworkRepository
) {
    suspend fun getEpgInfo(): List<EpgInfoResponse> {
        val infoResult = networkRepository.loadEpgInfo().channels

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
        }
    }

    private companion object {
        const val CHANNEL_NAME_SPLIT_DELIMITER = " • "
    }
}