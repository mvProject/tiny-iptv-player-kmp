/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:16
 *
 */

package com.mvproject.videoapp.di

import android.app.Application
import com.mvproject.videoapp.di.modules.appModule
import com.mvproject.videoapp.di.modules.helperModule
import com.mvproject.videoapp.di.modules.managerModule
import com.mvproject.videoapp.di.modules.networkModule
import com.mvproject.videoapp.di.modules.playerModule
import com.mvproject.videoapp.di.modules.repositoryModule
import com.mvproject.videoapp.di.modules.viewModelModule
import com.mvproject.videoapp.di.modules.workerModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Napier.base(DebugAntilog())

        startKoin {
            androidLogger()
            androidContext(this@App)
            workManagerFactory()
            modules(
                appModule,
                playerModule,
                networkModule,
                repositoryModule,
                helperModule,
                managerModule,
                viewModelModule,
                workerModule
            )
        }
    }
}
