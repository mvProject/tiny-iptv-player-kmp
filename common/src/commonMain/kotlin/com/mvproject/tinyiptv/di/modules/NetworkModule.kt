/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 27.10.23, 09:55
 *
 */

package com.mvproject.tinyiptv.di.modules

import com.mvproject.tinyiptv.data.network.NetworkRepository
import com.mvproject.tinyiptv.platform.createHttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val networkModule = module {
    single {
        createHttpClient()
    }
    singleOf(::NetworkRepository)
}