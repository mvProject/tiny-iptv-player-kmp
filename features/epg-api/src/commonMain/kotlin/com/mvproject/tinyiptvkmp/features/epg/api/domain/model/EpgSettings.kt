package com.mvproject.tinyiptvkmp.features.epg.api.domain.model

data class EpgSettings(
    val epgInfoDataLastUpdate: Long,
    val epgInfoUpdatePeriod: Int,
    val epgDataLastUpdate: Long,
    val epgUpdatePeriod: Int,
    val epgProgramClean: Long,
)
