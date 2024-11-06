/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 17.06.24, 11:12
 *
 */

package com.mvproject.tinyiptvkmp.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.mvproject.tinyiptvkmp.core.database.db.AppDatabase
import com.mvproject.tinyiptvkmp.core.database.db.dbFileName
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun databaseBuilderModule(): Module =
    module {
        single<RoomDatabase.Builder<AppDatabase>> { createRoomDatabaseBuilder(get()) }
    }

private fun createRoomDatabaseBuilder(ctx: Context): RoomDatabase.Builder<AppDatabase> {
    val dbFile = ctx.getDatabasePath(dbFileName)
    return Room
        .databaseBuilder<AppDatabase>(ctx, dbFile.absolutePath)
        .setDriver(AndroidSQLiteDriver())
}
