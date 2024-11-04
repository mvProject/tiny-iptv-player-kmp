/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 09.05.23, 12:46
 *
 */

package com.mvproject.tinyiptvkmp.di.modules

import com.mvproject.tinyiptvkmp.data.datasource.EpgChannelDatasource
import com.mvproject.tinyiptvkmp.data.datasource.EpgProgramDatasource
import com.mvproject.tinyiptvkmp.data.datasource.LocalPlaylistDataSource
import com.mvproject.tinyiptvkmp.data.datasource.RemotePlaylistDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataSourceModule =
    module {
        singleOf(::RemotePlaylistDataSource)
        singleOf(::LocalPlaylistDataSource)
        singleOf(::EpgChannelDatasource)
        singleOf(::EpgProgramDatasource)
    }
