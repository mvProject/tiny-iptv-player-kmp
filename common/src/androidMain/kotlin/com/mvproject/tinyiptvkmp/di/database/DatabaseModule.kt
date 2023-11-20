/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.di.database

import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.mvproject.tinyiptvkmp.TinyIptvKmpDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformDatabaseModule(): Module = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = TinyIptvKmpDatabase.Schema,
            context = androidContext(),
            name = "tinyiptvkmp.db",
            callback = object : AndroidSqliteDriver.Callback(TinyIptvKmpDatabase.Schema) {
                override fun onConfigure(db: SupportSQLiteDatabase) {
                    super.onConfigure(db)
                    setPragma(db, "JOURNAL_MODE = WAL")
                    setPragma(db, "SYNCHRONOUS = 2")
                }

                private fun setPragma(db: SupportSQLiteDatabase, pragma: String) {
                    val cursor = db.query("PRAGMA $pragma")
                    cursor.moveToFirst()
                    cursor.close()
                }
            }
        )
    }


    single {
        TinyIptvKmpDatabase(get())
    }
}