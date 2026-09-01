/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 13.06.24, 11:04
 *
 */

package com.mvproject.tinyiptvkmp.di

import com.mvproject.tinyiptvkmp.RootViewModel
import com.mvproject.tinyiptvkmp.core.datastore.di.appPreferencesModule
import com.mvproject.tinyiptvkmp.core.datastore.di.datastoreModule
import com.mvproject.tinyiptvkmp.core.network.di.networkModule
import com.mvproject.tinyiptvkmp.di.database.databaseModule
import com.mvproject.tinyiptvkmp.features.channels.api.di.channelsApiModule
import com.mvproject.tinyiptvkmp.features.channels.di.channelsModule
import com.mvproject.tinyiptvkmp.features.channels.nav.GroupChannelsNavigator
import com.mvproject.tinyiptvkmp.features.epg.api.di.epgApiModule
import com.mvproject.tinyiptvkmp.features.groups.api.di.groupsApiModule
import com.mvproject.tinyiptvkmp.features.groups.di.groupsModule
import com.mvproject.tinyiptvkmp.features.groups.nav.GroupNavigator
import com.mvproject.tinyiptvkmp.features.player.api.di.playerApiModule
import com.mvproject.tinyiptvkmp.features.player.di.playerModule
import com.mvproject.tinyiptvkmp.features.player.nav.PlayerNavigator
import com.mvproject.tinyiptvkmp.features.playlist.api.di.playlistApiModule
import com.mvproject.tinyiptvkmp.features.playlist.di.playlistModule
import com.mvproject.tinyiptvkmp.features.playlist.nav.PlaylistNavigator
import com.mvproject.tinyiptvkmp.features.settings.di.settingsModule
import com.mvproject.tinyiptvkmp.features.settings.nav.SettingsNavigator
import com.mvproject.tinyiptvkmp.infrastructure.logging.di.loggingModule
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.navigation.DefaultNavigator
import com.mvproject.tinyiptvkmp.navigation.Navigator
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.binds
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

private val appModule =
    module {
        includes(
            loggingModule,
            datastoreModule,
            appPreferencesModule,
            databaseModule,
            networkModule,
            channelsApiModule,
            groupsApiModule,
            epgApiModule,
            playlistApiModule,
            playerApiModule,
            channelsModule,
            groupsModule,
            playerModule,
            playlistModule,
            settingsModule,
        )

        viewModel<RootViewModel>()
    }

val navModule = module {
    single<Navigator> {
        DefaultNavigator(startDestination = AppRoutes.PlaylistGroup)
    } binds arrayOf(
        Navigator::class,
        SettingsNavigator::class,
        PlaylistNavigator::class,
        GroupNavigator::class,
        GroupChannelsNavigator::class,
        PlayerNavigator::class,
    )
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(appModule, navModule)
    }
