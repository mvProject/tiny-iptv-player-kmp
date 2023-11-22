/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.di.datasource

import com.mvproject.tinyiptvkmp.platform.LocalPlaylistDataSource
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformDataSourceModule(): Module = module {
    single {
        LocalPlaylistDataSource()
    }
}