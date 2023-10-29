/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 26.10.23, 19:35
 *
 */

package com.mvproject.tinyiptv.di

import com.mvproject.tinyiptv.di.database.platformDatabaseModule
import com.mvproject.tinyiptv.di.datastore.platformDataStoreModule
import com.mvproject.tinyiptv.di.modules.dataSourceModule
import com.mvproject.tinyiptv.di.modules.helperModule
import com.mvproject.tinyiptv.di.modules.networkModule
import com.mvproject.tinyiptv.di.modules.repositoryModule
import com.mvproject.tinyiptv.di.modules.useCaseModule
import com.mvproject.tinyiptv.di.modules.viewModelsModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            networkModule,
            repositoryModule,
            dataSourceModule,
            helperModule,
            useCaseModule,
            viewModelsModule,
            platformDatabaseModule(),
            platformDataStoreModule()
        )
    }