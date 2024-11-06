/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 13.06.24, 11:04
 *
 */

package com.mvproject.tinyiptvkmp.di

import com.mvproject.tinyiptvkmp.core.database.di.databaseBuilderModule
import com.mvproject.tinyiptvkmp.core.database.di.databaseModule
import com.mvproject.tinyiptvkmp.core.datastore.di.datastoreModule
import com.mvproject.tinyiptvkmp.core.network.di.networkModule
import com.mvproject.tinyiptvkmp.di.modules.repositoryModule
import com.mvproject.tinyiptvkmp.di.modules.useCaseModule
import com.mvproject.tinyiptvkmp.di.modules.viewModelsModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            datastoreModule(),
            databaseBuilderModule(),
            databaseModule,
            networkModule,
            repositoryModule,
            useCaseModule,
            viewModelsModule
        )
    }
