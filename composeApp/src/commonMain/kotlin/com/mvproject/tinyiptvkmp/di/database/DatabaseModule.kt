/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 13.06.24, 11:04
 *
 */

package com.mvproject.tinyiptvkmp.di.database

import com.mvproject.tinyiptvkmp.core.database.builder.createRoomDatabase
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
