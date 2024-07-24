/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.07.24, 16:02
 *
 */

package com.mvproject.tinyiptvkmp.data.repository

import androidx.room.Transaction
import com.mvproject.tinyiptvkmp.data.mappers.EntityMapper.toEpgInfo
import com.mvproject.tinyiptvkmp.data.mappers.ParseMappers.toEpgInfo
import com.mvproject.tinyiptvkmp.data.mappers.ParseMappers.toEpgInfoEntity
import com.mvproject.tinyiptvkmp.data.model.epg.EpgInfo
import com.mvproject.tinyiptvkmp.data.model.response.EpgInfoResponse
import com.mvproject.tinyiptvkmp.database.AppDatabase
import com.mvproject.tinyiptvkmp.utils.KLog
import kotlinx.coroutines.delay

class EpgInfoRepository(
    private val appDatabase: AppDatabase,
) {
    private val epgInfoDao = appDatabase.epgInfoDao()

    suspend fun loadEpgInfoData(): List<EpgInfo> =
        epgInfoDao.getEpgInfo().map {
            it.toEpgInfo()
        }

    @Transaction
    suspend fun saveEpgInfoDataRoom(infoData: List<EpgInfoResponse>) {
        val info = infoData.map { it.toEpgInfo() }
        KLog.i("testing addEpgInfoData Room count ${info.count()}")
        epgInfoDao.deleteEpgInfo()
        epgInfoDao.insertEpgInfo(data = info)

        delay(500L)

        val list = epgInfoDao.getEpgInfo()
        KLog.w("testing addEpgInfoData load from room ${list.count()}")
    }

    suspend fun updateEpgInfoUpdate(info: EpgInfo) {
        KLog.i("testing updateEpgInfoUpdate info $info")
        val infoEntity = info.toEpgInfoEntity()
        epgInfoDao.updateEpgInfo(data = infoEntity)
    }
}
