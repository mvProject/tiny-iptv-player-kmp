/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 17.05.24, 18:15
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
    private val appDatabase: AppDatabase,
) {
    private val epgInfoDao = appDatabase.epgInfoDao()

    suspend fun loadEpgInfoData(): List<EpgInfo> {
        return epgInfoDao.getEpgInfo().map {
            it.toEpgInfo()
        }
    }

    suspend fun saveEpgInfoDataRoom(infoData: List<EpgInfoResponse>) {
        KLog.i("testing addEpgInfoData Room")
        epgInfoDao.insertEpgInfo(data = infoData.map { it.toEpgInfo() })

        delay(500L)

        val list = epgInfoDao.getEpgInfo()
        KLog.w("testing addEpgInfoData load from room $list")
    }
}
