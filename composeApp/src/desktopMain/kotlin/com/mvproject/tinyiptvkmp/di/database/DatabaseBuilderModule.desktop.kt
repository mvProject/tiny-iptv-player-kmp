/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 17.06.24, 11:12
 *
 */

package com.mvproject.tinyiptvkmp.di.database

import androidx.room.RoomDatabase
import com.mvproject.tinyiptvkmp.core.data.database.db.AppDatabase
import com.mvproject.tinyiptvkmp.core.data.database.db.dbFileName
import com.mvproject.tinyiptvkmp.core.database.builder.createRoomDatabaseBuilder
import com.mvproject.tinyiptvkmp.core.database.config.DatabaseConfig
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun databaseBuilderModule(): Module =
    module {
        single<RoomDatabase.Builder<AppDatabase>> {
            createRoomDatabaseBuilder(DatabaseConfig(dbFileName))
        }
    }
