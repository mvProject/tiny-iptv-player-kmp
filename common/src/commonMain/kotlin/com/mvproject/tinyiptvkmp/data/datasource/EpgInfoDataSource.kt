/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.datasource

import com.mvproject.tinyiptvkmp.data.model.response.EpgInfoResponse
import com.mvproject.tinyiptvkmp.data.network.NetworkRepository

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