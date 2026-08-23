/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 13.06.24, 11:04
 *
 */

package com.mvproject.tinyiptvkmp.di

import com.mvproject.tinyiptvkmp.core.data.di.repositoryModule
import com.mvproject.tinyiptvkmp.core.database.di.databaseModule
import com.mvproject.tinyiptvkmp.core.datastore.di.appPreferencesModule
import com.mvproject.tinyiptvkmp.core.datastore.di.datastoreModule
import com.mvproject.tinyiptvkmp.core.domain.di.useCaseModule
import com.mvproject.tinyiptvkmp.core.network.di.networkModule
import com.mvproject.tinyiptvkmp.features.channels.di.channelsModule
import com.mvproject.tinyiptvkmp.features.groups.di.groupsModule
import com.mvproject.tinyiptvkmp.features.player.di.playerModule
import com.mvproject.tinyiptvkmp.features.playlist.di.playlistModule
import com.mvproject.tinyiptvkmp.features.settings.di.settingsModule
import com.mvproject.tinyiptvkmp.infrastructure.logging.di.loggingModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            loggingModule,
            datastoreModule,
            appPreferencesModule,
            databaseModule,
            networkModule,
            repositoryModule,
            useCaseModule,
            channelsModule,
            groupsModule,
            playerModule,
            playlistModule,
            settingsModule
        )
    }
