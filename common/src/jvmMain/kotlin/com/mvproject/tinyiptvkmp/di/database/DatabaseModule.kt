/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:19
 *
 */

package com.mvproject.tinyiptvkmp.di.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.mvproject.tinyiptvkmp.database.AppDatabase
import com.mvproject.tinyiptvkmp.database.dbFileName
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

actual fun platformDatabaseModule(): Module =
    module {
        single<AppDatabase> { createRoomDatabase() }
    }

fun createRoomDatabase(): AppDatabase {
    val dbFile = File(System.getProperty("java.io.tmpdir"), dbFileName)
    return Room.databaseBuilder<AppDatabase>(name = dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
        .build()
}
