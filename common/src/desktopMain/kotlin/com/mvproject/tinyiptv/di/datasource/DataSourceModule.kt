/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 27.10.23, 10:58
 *
 */

package com.mvproject.tinyiptv.di.datasource

import com.mvproject.tinyiptv.platform.LocalPlaylistDataSource
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformDataSourceModule(): Module = module {
    single {
        LocalPlaylistDataSource()
    }
}