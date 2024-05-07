/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:19
 *
 */

package com.mvproject.tinyiptvkmp.di.database

import android.content.Context
import androidx.room.ExperimentalRoomApi
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.mvproject.tinyiptvkmp.database.AppDatabase
import com.mvproject.tinyiptvkmp.database.dbFileName
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformDatabaseModule(): Module =
    module {
        single<AppDatabase> { createRoomDatabase(get()) }
    }

@OptIn(ExperimentalRoomApi::class)
fun createRoomDatabase(ctx: Context): AppDatabase {
    val dbFile = ctx.getDatabasePath(dbFileName)
    return Room.databaseBuilder<AppDatabase>(ctx, dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
        // .setAutoCloseTimeout(
        //     autoCloseTimeout = 5,
        //     autoCloseTimeUnit = TimeUnit.MINUTES,
        // )
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
