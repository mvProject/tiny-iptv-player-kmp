/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 17.05.24, 18:15
 *
 */

package com.mvproject.tinyiptvkmp.di.modules

import com.mvproject.tinyiptvkmp.data.repository.EpgInfoRepository
import com.mvproject.tinyiptvkmp.data.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule =
    module {
        singleOf(::PreferenceRepository)
        singleOf(::PlaylistsRepository)
        singleOf(::PlaylistChannelsRepository)
        singleOf(::EpgProgramRepository)
        singleOf(::EpgInfoRepository)
        singleOf(::FavoriteChannelsRepository)
    }
