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
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

val networkModule = module {
    single { createHttpClient() }
    single<EpgChannelDatasource>()
    single<EpgProgramDatasource>()
    single<NetworkPlaylistDatasource>()
}
