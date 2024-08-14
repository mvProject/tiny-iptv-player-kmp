/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.07.24, 16:02
 *
 */

package com.mvproject.tinyiptvkmp.data.repository

import com.mvproject.tinyiptvkmp.data.mappers.EntityMapper.toEpgInfo
import com.mvproject.tinyiptvkmp.data.mappers.ParseMappers.toEpgInfoEntity
import com.mvproject.tinyiptvkmp.data.model.epg.EpgInfo
import com.mvproject.tinyiptvkmp.database.AppDatabase

class EpgInfoRepository(
    private val appDatabase: AppDatabase,
) {
    private val epgInfoDao = appDatabase.epgInfoDao()

    suspend fun loadEpgInfoData(): List<EpgInfo> =
        epgInfoDao.getEpgInfo().map {
            it.toEpgInfo()
        }

    suspend fun saveEpgInfoData(info: List<EpgInfo>) {
        val infoData = info.map { it.toEpgInfoEntity() }.toTypedArray()
        epgInfoDao.saveEpgInfo(info = infoData)
    }
}
