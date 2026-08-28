package com.mvproject.tinyiptvkmp.core.database.builder

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.mvproject.tinyiptvkmp.core.database.config.DatabaseConfig
import java.io.File

inline fun <reified T : RoomDatabase> createRoomDatabaseBuilder(
    config: DatabaseConfig,
): RoomDatabase.Builder<T> {
    val appDirectory =
        File(System.getProperty("user.home"), ".tinyiptv").apply {
            mkdirs()
        }
    val dbFile = File(appDirectory, config.name)

    return Room
        .databaseBuilder<T>(name = dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
}
