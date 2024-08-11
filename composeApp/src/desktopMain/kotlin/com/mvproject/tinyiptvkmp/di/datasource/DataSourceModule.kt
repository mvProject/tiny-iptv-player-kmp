/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 22.11.23, 18:54
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