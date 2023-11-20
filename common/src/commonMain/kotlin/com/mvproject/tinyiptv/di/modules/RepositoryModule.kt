/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 09.05.23, 12:46
 *
 */

package com.mvproject.tinyiptv.di.modules

import com.mvproject.tinyiptv.data.repository.EpgInfoRepository
import com.mvproject.tinyiptv.data.repository.EpgProgramRepository
import com.mvproject.tinyiptv.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptv.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptv.data.repository.PlaylistsRepository
import com.mvproject.tinyiptv.data.repository.PreferenceRepository
import com.mvproject.tinyiptv.data.repository.SelectedEpgRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::PreferenceRepository)
    singleOf(::PlaylistsRepository)
    singleOf(::PlaylistChannelsRepository)
    singleOf(::EpgProgramRepository)
    singleOf(::EpgInfoRepository)
    singleOf(::FavoriteChannelsRepository)
    singleOf(::SelectedEpgRepository)
}