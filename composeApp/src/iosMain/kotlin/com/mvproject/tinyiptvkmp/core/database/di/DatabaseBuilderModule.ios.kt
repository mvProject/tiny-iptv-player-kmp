package com.mvproject.tinyiptvkmp.core.database.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.NativeSQLiteDriver
import com.mvproject.tinyiptvkmp.core.database.db.AppDatabase
import com.mvproject.tinyiptvkmp.core.database.db.dbFileName
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual fun databaseBuilderModule(): Module =
    module {
        single<RoomDatabase.Builder<AppDatabase>> { createRoomDatabaseBuilder() }
    }

@OptIn(ExperimentalForeignApi::class)
private fun createRoomDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )

    val dbFilePath = documentDirectory?.path + "/$dbFileName"

    return Room
        .databaseBuilder<AppDatabase>(name = dbFilePath)
        .setDriver(NativeSQLiteDriver())
}
