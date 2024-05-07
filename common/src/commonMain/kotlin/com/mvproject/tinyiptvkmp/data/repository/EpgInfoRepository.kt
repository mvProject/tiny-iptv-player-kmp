/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:29
 *
 */

package com.mvproject.tinyiptvkmp.data.repository

import com.mvproject.tinyiptvkmp.data.mappers.EntityMapper.toEpgInfo
import com.mvproject.tinyiptvkmp.data.mappers.ParseMappers.toEpgInfo
import com.mvproject.tinyiptvkmp.data.model.epg.EpgInfo
import com.mvproject.tinyiptvkmp.data.model.response.EpgInfoResponse
import com.mvproject.tinyiptvkmp.database.AppDatabase
import com.mvproject.tinyiptvkmp.utils.KLog
import kotlinx.coroutines.delay

class EpgInfoRepository(
    //   private val db: TinyIptvKmpDatabase,
    private val appDatabase: AppDatabase,
) {
    //  private val epgInfoQueries = db.epgInfoEntityQueries
    private val epgInfoDao = appDatabase.epgInfoDao()

    /*    fun loadEpgInfoData(): List<EpgInfo> {
            return epgInfoQueries.getEpgInfoEntity()
                .executeAsList()
                .map { entity ->
                    entity.toEpgInfo()
                }
        }*/

    suspend fun loadEpgInfoData(): List<EpgInfo> {
        return epgInfoDao.getEpgInfo().map {
            it.toEpgInfo()
        }
    }

    /*    suspend fun addEpgInfoData(infoData: List<EpgInfoResponse>) {
            KLog.i("addEpgInfoData")
            withContext(Dispatchers.IO) {
                epgInfoQueries.transaction {
                    infoData.forEach { item ->
                        try {
                            epgInfoQueries.addEpgInfoEntity(
                                item.toEpgInfoEntity(),
                            )
                        } catch (ex: Exception) {
                            KLog.e("addEpgInfoData Exception ${ex.localizedMessage}")
                            KLog.e("addEpgInfoData Exception id:${item.channelId}, name:${item.channelNames}")
                        }
                    }
                }
            }
        }*/

    suspend fun saveEpgInfoDataRoom(infoData: List<EpgInfoResponse>) {
        KLog.i("testing addEpgInfoData Room")
        epgInfoDao.insertEpgInfo(data = infoData.map { it.toEpgInfo() })

        delay(500L)

        val list = epgInfoDao.getEpgInfo()
        KLog.w("testing addEpgInfoData load from room $list")
    }

    /*    suspend fun updateEpgInfoData(infoData: List<EpgInfoResponse>) {
            KLog.i("updateEpgInfoData")
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
                            KLog.e("updateEpgInfoData Exception ${ex.localizedMessage}")
                            KLog.e("updateEpgInfoData Exception id:${item.channelId}, name:${item.channelNames}")
                        }
                    }
                }
            }
        }*/
}
