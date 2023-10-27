/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 26.10.23, 19:38
 *
 */

package com.mvproject.tinyiptv.di.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.mvproject.tinyiptv.TinyIptvDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformDatabaseModule(): Module = module {
    single<SqlDriver> {
        JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
            TinyIptvDatabase.Schema.create(this)
        }
    }


    single {
        TinyIptvDatabase(get())
    }
}