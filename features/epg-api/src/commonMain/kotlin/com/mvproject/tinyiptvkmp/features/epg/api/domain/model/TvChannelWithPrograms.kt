package com.mvproject.tinyiptvkmp.features.epg.api.domain.model

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.TvChannel

data class TvChannelWithPrograms(
    val channel: TvChannel = TvChannel(),
    val programs: List<EpgProgram> = emptyList(),
) {
    val channelName: String
        get() = channel.channelName

    val channelUrl: String
        get() = channel.channelUrl

    val channelLogo: String
        get() = channel.channelLogo

    val programId: String
        get() = channel.programId

    val favoriteType: String
        get() = channel.favoriteType
}

fun TvChannel.withPrograms(programs: List<EpgProgram> = emptyList()) =
    TvChannelWithPrograms(
        channel = this,
        programs = programs,
    )
