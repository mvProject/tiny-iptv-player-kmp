/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 28.10.23, 21:19
 *
 */

package com.mvproject.tinyiptv

import android.app.Application
import com.mvproject.tinyiptv.di.initKoin
import com.mvproject.tinyiptv.di.modules.imageModule
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
                imageModule
            )
        }
    }
}