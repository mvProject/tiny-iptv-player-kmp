package com.mvproject.tinyiptvkmp.core.database.builder

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.mvproject.tinyiptvkmp.core.database.config.DatabaseConfig

inline fun <reified T : RoomDatabase> createRoomDatabaseBuilder(
    context: Context,
    config: DatabaseConfig,
): RoomDatabase.Builder<T> {
    val dbFile = context.getDatabasePath(config.name)
    return Room
        .databaseBuilder<T>(context, dbFile.absolutePath)
        .setDriver(AndroidSQLiteDriver())
}
