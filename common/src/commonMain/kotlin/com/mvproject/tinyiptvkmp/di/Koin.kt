/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.di

import com.mvproject.tinyiptvkmp.di.database.platformDatabaseModule
import com.mvproject.tinyiptvkmp.di.datasource.platformDataSourceModule
import com.mvproject.tinyiptvkmp.di.datastore.platformDataStoreModule
import com.mvproject.tinyiptvkmp.di.modules.dataSourceModule
import com.mvproject.tinyiptvkmp.di.modules.helperModule
import com.mvproject.tinyiptvkmp.di.modules.networkModule
import com.mvproject.tinyiptvkmp.di.modules.repositoryModule
import com.mvproject.tinyiptvkmp.di.modules.useCaseModule
import com.mvproject.tinyiptvkmp.di.modules.viewModelsModule
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
            platformDataStoreModule(),
            platformDataSourceModule()
        )
    }