/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:16
 *
 */

package com.mvproject.tinyiptv.di

import android.app.Application
import com.mvproject.tinyiptv.di.modules.imageModule
import com.mvproject.tinyiptv.di.modules.viewModelModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Napier.base(DebugAntilog())

        initKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                viewModelModule,
                imageModule
            )
        }
    }
}
