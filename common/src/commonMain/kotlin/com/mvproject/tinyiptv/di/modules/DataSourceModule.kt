/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 09.05.23, 12:46
 *
 */

package com.mvproject.tinyiptv.di.modules

import com.mvproject.tinyiptv.data.datasource.EpgDataSource
import com.mvproject.tinyiptv.data.datasource.EpgInfoDataSource
import com.mvproject.tinyiptv.data.datasource.RemotePlaylistDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataSourceModule = module {
    singleOf(::RemotePlaylistDataSource)
    singleOf(::EpgInfoDataSource)
    singleOf(::EpgDataSource)
}