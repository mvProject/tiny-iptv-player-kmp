package com.mvproject.tinyiptvkmp.persistence.epg.room.di

import com.mvproject.tinyiptvkmp.features.epg.api.data.datasource.local.EpgChannelLocalDataSource
import com.mvproject.tinyiptvkmp.features.epg.api.data.datasource.local.EpgProgramLocalDataSource
import com.mvproject.tinyiptvkmp.persistence.epg.room.RoomEpgChannelLocalDataSource
import com.mvproject.tinyiptvkmp.persistence.epg.room.RoomEpgProgramLocalDataSource
import org.koin.dsl.module

val epgRoomModule =
    module {
        single<EpgChannelLocalDataSource> { RoomEpgChannelLocalDataSource(get()) }
        single<EpgProgramLocalDataSource> { RoomEpgProgramLocalDataSource(get()) }
    }
