package com.mvproject.tinyiptvkmp.features.epg.api.data.remote

import com.mvproject.tinyiptvkmp.core.network.data.parse.ProgramParsed
import com.mvproject.tinyiptvkmp.core.network.datasource.EpgProgramDatasource

internal class EpgProgramRemoteDataSourceImpl(
    private val epgProgramDatasource: EpgProgramDatasource,
) : EpgProgramRemoteDataSource {
    override suspend fun downloadAndParseXml(
        url: String,
        onProgrammeParsed: suspend (ProgramParsed) -> Unit,
    ) {
        epgProgramDatasource.downloadAndParseXml(
            url = url,
            onProgrammeParsed = onProgrammeParsed,
        )
    }
}
