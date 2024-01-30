/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 22.11.23, 18:54
 *
 */

package com.mvproject.tinyiptvkmp.di.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.mvproject.tinyiptvkmp.TinyIptvKmpDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformDatabaseModule(): Module = module {
    single<SqlDriver> {
        JdbcSqliteDriver(url = "jdbc:sqlite:tinyiptvkmp.db", schema = TinyIptvKmpDatabase.Schema)
    }


    single {
        TinyIptvKmpDatabase(get())
    }
}