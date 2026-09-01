package com.mvproject.tinyiptvkmp.core.database.di

import com.mvproject.tinyiptvkmp.core.database.builder.createRoomDatabase
import com.mvproject.tinyiptvkmp.core.database.db.AppDatabase
import com.mvproject.tinyiptvkmp.persistence.channels.room.database.FavoriteChannelDao
import com.mvproject.tinyiptvkmp.persistence.channels.room.database.PlaylistChannelDao
import com.mvproject.tinyiptvkmp.persistence.epg.room.database.EpgChannelDao
import com.mvproject.tinyiptvkmp.persistence.epg.room.database.EpgProgramDao
import com.mvproject.tinyiptvkmp.persistence.playlist.room.database.PlaylistDao
import org.koin.dsl.module

val databaseModule =
    module {
        includes(databaseBuilderModule())
        single<AppDatabase> {
            createRoomDatabase(get())
        }
        single<EpgChannelDao> { get<AppDatabase>().epgInfoDao() }
        single<EpgProgramDao> { get<AppDatabase>().epgProgramDao() }
        single<FavoriteChannelDao> { get<AppDatabase>().favoriteChannelDao() }
        single<PlaylistDao> { get<AppDatabase>().playlistDao() }
        single<PlaylistChannelDao> { get<AppDatabase>().playlistChannelDao() }
    }
