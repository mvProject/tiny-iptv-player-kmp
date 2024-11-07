/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.core.network.di


import com.mvproject.tinyiptvkmp.core.network.client.createHttpClient
import com.mvproject.tinyiptvkmp.core.network.datasource.EpgChannelDatasource
import com.mvproject.tinyiptvkmp.core.network.datasource.EpgProgramDatasource
import com.mvproject.tinyiptvkmp.core.network.datasource.NetworkPlaylistDatasource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val networkModule = module {
    single { createHttpClient() }
    singleOf(::EpgChannelDatasource)
    singleOf(::EpgProgramDatasource)
    singleOf(::NetworkPlaylistDatasource)
}