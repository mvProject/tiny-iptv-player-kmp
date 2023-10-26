/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 20:59
 *
 */

package com.mvproject.tinyiptv.data.repository

import com.mvproject.tinyiptv.TinyIptvDatabase
import com.mvproject.tinyiptv.data.mappers.EntityMapper.toEpgInfo
import com.mvproject.tinyiptv.data.mappers.ParseMappers.toEpgInfoEntity
import com.mvproject.tinyiptv.data.model.epg.EpgInfo
import com.mvproject.tinyiptv.data.model.response.EpgInfoResponse
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EpgInfoRepository(
    private val db: TinyIptvDatabase
) {
    private val epgInfoQueries = db.epgInfoEntityQueries
    /*    suspend fun getEpgInfo(): List<AvailableChannelParseModel> {
            val infoResult = networkRepository.loadAlterInfo().channels

            val filtered =
                infoResult.filter { it.channelId.isNotEmpty() && it.channelIcon.isNotEmpty() }

            return buildList {
                filtered.forEach { chn ->
                    if (chn.channelNames.contains(CHANNEL_NAME_SPLIT_DELIMITER)) {
                        val splitNames = chn.channelNames.split(CHANNEL_NAME_SPLIT_DELIMITER)
                        splitNames.forEach { spl ->
                            add(
                                AvailableChannelParseModel(
                                    channelId = chn.channelId,
                                    channelIcon = chn.channelIcon,
                                    channelNames = spl
                                )
                            )
                        }
                    } else add(chn)
                }
            }
        }*/

    /*    suspend fun saveEpgInfo(ephInfo: List<AvailableChannelParseModel>) {
            Napier.w("testing saveEpgInfo")
            addEpgInfoData(infoData = ephInfo)
            preferenceRepository.apply {
                setEpgInfoDataExist(state = true)
                setEpgInfoDataLastUpdate(timestamp = actualDate)
            }
            Napier.i("testing saveEpgInfo complete ${ephInfo.count()}")
        }

        suspend fun updateEpgInfo(ephInfo: List<AvailableChannelParseModel>) {
            Napier.w("testing updateEpgInfo")
            updateEpgInfoData(infoData = ephInfo)
            preferenceRepository.apply {
                setEpgInfoDataLastUpdate(timestamp = actualDate)
            }
            Napier.i("testing updateEpgInfo complete ${ephInfo.count()}")
        }*/

    fun loadEpgInfoData(): List<EpgInfo> {
        return epgInfoQueries.getEpgInfoEntity()
            .executeAsList()
            .map { entity ->
                entity.toEpgInfo()
            }
    }

    /*    private suspend fun parseChannelInfoMainData(): List<ChannelsInfoParseModel> {
            val resultStream = networkRepository.loadMainInfo().toInputStream()

            return buildList {
                BufferedReader(
                    InputStreamReader(resultStream)
                ).use {
                    it.readText().also { content ->
                        val result = M3UParser.parseInfo(content)
                        addAll(result)
                    }
                }
            }
        }*/

    suspend fun addEpgInfoData(infoData: List<EpgInfoResponse>) {
        Napier.w("testing addEpgInfoData")
        withContext(Dispatchers.IO) {
            epgInfoQueries.transaction {
                infoData.forEach { item ->
                    try {
                        epgInfoQueries.addEpgInfoEntity(
                            item.toEpgInfoEntity()
                        )
                    } catch (ex: Exception) {
                        Napier.e("testing addEpgInfoData Exception ${ex.localizedMessage}")
                        Napier.e("testing addEpgInfoData Exception id:${item.channelId}, name:${item.channelNames}")
                    }
                }
            }
        }
    }

    suspend fun updateEpgInfoData(infoData: List<EpgInfoResponse>) {
        Napier.w("testing updateEpgInfoData")
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
                        Napier.e("testing updateEpgInfoData Exception ${ex.localizedMessage}")
                        Napier.e("testing updateEpgInfoData Exception id:${item.channelId}, name:${item.channelNames}")
                    }
                }
            }
        }
    }
    /*
        private companion object {
            const val CHANNEL_NAME_SPLIT_DELIMITER = " • "
        }*/
}


