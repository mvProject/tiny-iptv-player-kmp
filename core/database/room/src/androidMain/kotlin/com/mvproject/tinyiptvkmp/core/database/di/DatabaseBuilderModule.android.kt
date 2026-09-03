package com.mvproject.tinyiptvkmp.core.database.di

import androidx.room.RoomDatabase
import com.mvproject.tinyiptvkmp.core.database.builder.createRoomDatabaseBuilder
import com.mvproject.tinyiptvkmp.core.database.config.DatabaseConfig
import com.mvproject.tinyiptvkmp.core.database.db.AppDatabase
import com.mvproject.tinyiptvkmp.core.database.db.dbFileName
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun databaseBuilderModule(): Module =
    module {
        single<RoomDatabase.Builder<AppDatabase>> {
            createRoomDatabaseBuilder(get(), DatabaseConfig(dbFileName))
        }
    }
