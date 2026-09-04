/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 31.01.24, 10:01
 *
 */

package com.mvproject.tinyiptvkmp

import android.app.Application
import android.content.pm.ApplicationInfo
import com.mvproject.tinyiptvkmp.di.initKoin
import com.mvproject.tinyiptvkmp.infrastructure.logging.AppLoggingConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        AppLoggingConfig.applyDefaults(
            isDebug = applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0,
        )
        initKoin {
            androidLogger()
            androidContext(this@App)
        }
    }
}
