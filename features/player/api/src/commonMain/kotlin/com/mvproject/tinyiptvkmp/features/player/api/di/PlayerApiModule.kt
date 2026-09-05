package com.mvproject.tinyiptvkmp.features.player.api.di

import com.mvproject.tinyiptvkmp.core.datastore.DataStorePathProvider
import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import com.mvproject.tinyiptvkmp.core.datastore.ProtoStoreFactory
import com.mvproject.tinyiptvkmp.features.player.api.data.repository.PlayerRepositoryImpl
import com.mvproject.tinyiptvkmp.features.player.api.data.storage.PlayerLocalDataSource
import com.mvproject.tinyiptvkmp.features.player.api.data.storage.PlayerLocalDataSourceImpl
import com.mvproject.tinyiptvkmp.features.player.api.data.storage.PlayerPreferencesProto
import com.mvproject.tinyiptvkmp.features.player.api.data.storage.PlayerPreferencesSerializer
import com.mvproject.tinyiptvkmp.features.player.api.domain.repository.PlayerRepository
import com.mvproject.tinyiptvkmp.features.player.api.domain.usecase.GetPlayerSettingsUseCase
import com.mvproject.tinyiptvkmp.features.player.api.domain.usecase.GetPlayerSettingsUseCaseImpl
import com.mvproject.tinyiptvkmp.features.player.api.domain.usecase.ObservePlayerSettingsUseCase
import com.mvproject.tinyiptvkmp.features.player.api.domain.usecase.ObservePlayerSettingsUseCaseImpl
import com.mvproject.tinyiptvkmp.features.player.api.domain.usecase.UpdateFullscreenModeUseCase
import com.mvproject.tinyiptvkmp.features.player.api.domain.usecase.UpdateFullscreenModeUseCaseImpl
import com.mvproject.tinyiptvkmp.features.player.api.domain.usecase.UpdateVideoSizeUseCase
import com.mvproject.tinyiptvkmp.features.player.api.domain.usecase.UpdateVideoSizeUseCaseImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val PLAYER_PREFERENCES = "playerPreferences"
private const val PLAYER_PREFERENCES_FILE_NAME = "tiny_iptv_player_settings.pb"

val playerApiModule =
    module {
        single<ProtoStore<PlayerPreferencesProto>>(named(PLAYER_PREFERENCES)) {
            ProtoStoreFactory.create(
                serializer = PlayerPreferencesSerializer,
                producePath = {
                    get<DataStorePathProvider>().providePath(PLAYER_PREFERENCES_FILE_NAME)
                },
            )
        }
        single<PlayerLocalDataSource> {
            PlayerLocalDataSourceImpl(get(named(PLAYER_PREFERENCES)))
        }
        single<PlayerRepository> { PlayerRepositoryImpl(get()) }
        single<GetPlayerSettingsUseCase> { GetPlayerSettingsUseCaseImpl(get()) }
        single<ObservePlayerSettingsUseCase> { ObservePlayerSettingsUseCaseImpl(get()) }
        single<UpdateFullscreenModeUseCase> { UpdateFullscreenModeUseCaseImpl(get()) }
        single<UpdateVideoSizeUseCase> { UpdateVideoSizeUseCaseImpl(get()) }
    }
