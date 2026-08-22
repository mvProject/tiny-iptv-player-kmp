package com.mvproject.tinyiptvkmp.core.database.builder

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.NativeSQLiteDriver
import com.mvproject.tinyiptvkmp.core.database.config.DatabaseConfig
import platform.Foundation.NSHomeDirectory

inline fun <reified T : RoomDatabase> createRoomDatabaseBuilder(
    config: DatabaseConfig,
): RoomDatabase.Builder<T> {
    val dbFilePath = "${NSHomeDirectory()}/Documents/${config.name}"

    return Room
        .databaseBuilder<T>(name = dbFilePath)
        .setDriver(NativeSQLiteDriver())
}
