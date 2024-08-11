/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 17.06.24, 11:12
 *
 */

package com.mvproject.tinyiptvkmp.di.database

import android.content.Context
import androidx.room.ExperimentalRoomApi
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mvproject.tinyiptvkmp.database.AppDatabase
import com.mvproject.tinyiptvkmp.database.dbFileName
import org.koin.core.module.Module
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

actual fun platformDatabaseModule(): Module =
    module {
        single<RoomDatabase.Builder<AppDatabase>> { createRoomDatabaseBuilder(get()) }
    }

@OptIn(ExperimentalRoomApi::class)
fun createRoomDatabaseBuilder(ctx: Context): RoomDatabase.Builder<AppDatabase> {
    val dbFile = ctx.getDatabasePath(dbFileName)
    return Room
        .databaseBuilder<AppDatabase>(ctx, dbFile.absolutePath)
        .setAutoCloseTimeout(
            autoCloseTimeout = 3600,
            autoCloseTimeUnit = TimeUnit.SECONDS,
        )
}
