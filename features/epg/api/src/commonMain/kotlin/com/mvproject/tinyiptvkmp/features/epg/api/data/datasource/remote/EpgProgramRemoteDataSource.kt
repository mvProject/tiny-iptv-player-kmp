package com.mvproject.tinyiptvkmp.features.epg.api.data.datasource.remote

import com.mvproject.tinyiptvkmp.core.network.data.parse.ProgramParsed

internal interface EpgProgramRemoteDataSource {
    suspend fun downloadAndParseXml(
        url: String,
        onProgrammeParsed: suspend (ProgramParsed) -> Unit,
    )
}
