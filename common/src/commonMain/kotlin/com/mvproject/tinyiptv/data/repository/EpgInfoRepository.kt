/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 26.10.23, 15:44
 *
 */

package com.mvproject.tinyiptv.data.repository

import com.mvproject.tinyiptv.TinyIptvDatabase
import com.mvproject.tinyiptv.data.mappers.EntityMapper.toEpgInfo
import com.mvproject.tinyiptv.data.mappers.ParseMappers.toEpgInfoEntity
import com.mvproject.tinyiptv.data.model.epg.EpgInfo
import com.mvproject.tinyiptv.data.model.response.EpgInfoResponse
import com.mvproject.tinyiptv.utils.KLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EpgInfoRepository(
    private val db: TinyIptvDatabase
) {
    private val epgInfoQueries = db.epgInfoEntityQueries

    fun loadEpgInfoData(): List<EpgInfo> {
        return epgInfoQueries.getEpgInfoEntity()
            .executeAsList()
            .map { entity ->
                entity.toEpgInfo()
            }
    }

    suspend fun addEpgInfoData(infoData: List<EpgInfoResponse>) {
        KLog.w("testing addEpgInfoData")
        withContext(Dispatchers.IO) {
            epgInfoQueries.transaction {
                infoData.forEach { item ->
                    try {
                        epgInfoQueries.addEpgInfoEntity(
                            item.toEpgInfoEntity()
                        )
                    } catch (ex: Exception) {
                        KLog.e("testing addEpgInfoData Exception ${ex.localizedMessage}")
                        KLog.e("testing addEpgInfoData Exception id:${item.channelId}, name:${item.channelNames}")
                    }
                }
            }
        }
    }

    suspend fun updateEpgInfoData(infoData: List<EpgInfoResponse>) {
        KLog.w("testing updateEpgInfoData")
        withContext(Dispatchers.IO) {
            epgInfoQueries.transaction {
                infoData.forEach { item ->
                    try {
                        epgInfoQueries.updateEpgInfoEntity(
                            channelId = item.channelId,
                            channelName = item.channelNames.trim(),
                            channelLogo = item.channelIcon,
                        )
                    } catch (ex: Exception) {
                        KLog.e("testing updateEpgInfoData Exception ${ex.localizedMessage}")
                        KLog.e("testing updateEpgInfoData Exception id:${item.channelId}, name:${item.channelNames}")
                    }
                }
            }
        }
    }
}


