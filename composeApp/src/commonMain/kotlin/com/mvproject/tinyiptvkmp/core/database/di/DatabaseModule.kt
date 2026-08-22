/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 13.06.24, 11:04
 *
 */

package com.mvproject.tinyiptvkmp.core.database.di

import com.mvproject.tinyiptvkmp.core.database.builder.createRoomDatabase
import com.mvproject.tinyiptvkmp.core.database.db.AppDatabase
import org.koin.dsl.module

val databaseModule =
    module {
        includes(databaseBuilderModule())
        single<AppDatabase> {
            createRoomDatabase(get())
        }
    }
