/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:06
 *
 */

package com.mvproject.tinyiptvkmp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.mvproject.tinyiptvkmp.database.dao.EpgInfoDao
import com.mvproject.tinyiptvkmp.database.dao.EpgProgramDao
import com.mvproject.tinyiptvkmp.database.dao.FavoriteChannelDao
import com.mvproject.tinyiptvkmp.database.dao.PlaylistChannelDao
import com.mvproject.tinyiptvkmp.database.dao.PlaylistDao
import com.mvproject.tinyiptvkmp.database.entity.EpgInfoEntity
import com.mvproject.tinyiptvkmp.database.entity.EpgProgramEntity
import com.mvproject.tinyiptvkmp.database.entity.FavoriteChannelEntity
import com.mvproject.tinyiptvkmp.database.entity.PlaylistChannelEntity
import com.mvproject.tinyiptvkmp.database.entity.PlaylistEntity
import kotlinx.datetime.LocalDateTime

@Database(
    entities = [
        EpgInfoEntity::class,
        EpgProgramEntity::class,
        FavoriteChannelEntity::class,
        PlaylistEntity::class,
        PlaylistChannelEntity::class,
    ],
    version = 1,
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun epgInfoDao(): EpgInfoDao

    abstract fun epgProgramDao(): EpgProgramDao

    abstract fun favoriteChannelDao(): FavoriteChannelDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun playlistChannelDao(): PlaylistChannelDao
}

internal const val dbFileName = "tinyiptvkmp.db"

class LocalDateTimeConverter {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}
