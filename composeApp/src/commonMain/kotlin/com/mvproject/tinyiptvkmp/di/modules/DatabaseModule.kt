/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 13.06.24, 11:04
 *
 */

package com.mvproject.tinyiptvkmp.di.modules

import androidx.room.RoomDatabase
import com.mvproject.tinyiptvkmp.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val databaseModule =
    module {
        single<AppDatabase> {
            createRoomDatabase(get())
        }
    }

fun createRoomDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase =
    builder
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
