/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 26.10.23, 21:51
 *
 */

package com.mvproject.tinyiptv.di.database

import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.mvproject.tinyiptv.TinyIptvDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformDatabaseModule(): Module = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = TinyIptvDatabase.Schema,
            context = androidContext(),
            name = "tinyiptv.db",
            callback = object : AndroidSqliteDriver.Callback(TinyIptvDatabase.Schema) {
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
        TinyIptvDatabase(get())
    }
}