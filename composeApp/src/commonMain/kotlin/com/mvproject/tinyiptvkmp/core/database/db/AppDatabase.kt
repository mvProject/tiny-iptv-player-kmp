/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:06
 *
 */

package com.mvproject.tinyiptvkmp.core.database.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.mvproject.tinyiptvkmp.core.database.dao.EpgChannelDao
import com.mvproject.tinyiptvkmp.core.database.dao.EpgProgramDao
import com.mvproject.tinyiptvkmp.core.database.dao.FavoriteChannelDao
import com.mvproject.tinyiptvkmp.core.database.dao.PlaylistChannelDao
import com.mvproject.tinyiptvkmp.core.database.dao.PlaylistDao
import com.mvproject.tinyiptvkmp.core.database.entity.EpgChannelEntity
import com.mvproject.tinyiptvkmp.core.database.entity.EpgProgramEntity
import com.mvproject.tinyiptvkmp.core.database.entity.FavoriteChannelEntity
import com.mvproject.tinyiptvkmp.core.database.entity.PlaylistChannelEntity
import com.mvproject.tinyiptvkmp.core.database.entity.PlaylistEntity

@Database(
    entities = [
        EpgChannelEntity::class,
        EpgProgramEntity::class,
        FavoriteChannelEntity::class,
        PlaylistEntity::class,
        PlaylistChannelEntity::class,
    ],
    version = 1,
)

@ConstructedBy(AppDatabaseCtor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun epgInfoDao(): EpgChannelDao

    abstract fun epgProgramDao(): EpgProgramDao

    abstract fun favoriteChannelDao(): FavoriteChannelDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun playlistChannelDao(): PlaylistChannelDao
}

internal const val dbFileName = "tinyiptvkmp.db"

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseCtor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
