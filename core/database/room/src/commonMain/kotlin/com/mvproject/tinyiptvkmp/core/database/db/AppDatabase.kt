package com.mvproject.tinyiptvkmp.core.database.db

import androidx.room.AutoMigration
import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.mvproject.tinyiptvkmp.persistence.channels.room.database.FavoriteChannelDao
import com.mvproject.tinyiptvkmp.persistence.channels.room.database.FavoriteChannelEntity
import com.mvproject.tinyiptvkmp.persistence.channels.room.database.PlaylistChannelDao
import com.mvproject.tinyiptvkmp.persistence.channels.room.database.PlaylistChannelEntity
import com.mvproject.tinyiptvkmp.persistence.epg.room.database.EpgChannelDao
import com.mvproject.tinyiptvkmp.persistence.epg.room.database.EpgChannelEntity
import com.mvproject.tinyiptvkmp.persistence.epg.room.database.EpgProgramDao
import com.mvproject.tinyiptvkmp.persistence.epg.room.database.EpgProgramEntity
import com.mvproject.tinyiptvkmp.persistence.playlist.room.database.PlaylistDao
import com.mvproject.tinyiptvkmp.persistence.playlist.room.database.PlaylistEntity

@Database(
    entities = [
        EpgChannelEntity::class,
        EpgProgramEntity::class,
        FavoriteChannelEntity::class,
        PlaylistEntity::class,
        PlaylistChannelEntity::class,
    ],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
    ],
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
