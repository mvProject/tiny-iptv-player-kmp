/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 17.06.24, 11:12
 *
 */

package com.mvproject.tinyiptvkmp.core.database.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.mvproject.tinyiptvkmp.core.database.db.AppDatabase
import com.mvproject.tinyiptvkmp.core.database.db.dbFileName
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

actual fun databaseBuilderModule(): Module =
    module {
        single<RoomDatabase.Builder<AppDatabase>> { createRoomDatabaseBuilder() }
    }

private fun createRoomDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), dbFileName)
    return Room
        .databaseBuilder<AppDatabase>(name = dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
}
