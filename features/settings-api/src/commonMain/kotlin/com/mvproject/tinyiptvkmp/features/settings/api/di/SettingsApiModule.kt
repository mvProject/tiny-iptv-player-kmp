package com.mvproject.tinyiptvkmp.features.settings.api.di

import com.mvproject.tinyiptvkmp.features.settings.api.data.local.SettingsLocalDataSource
import com.mvproject.tinyiptvkmp.features.settings.api.data.local.SettingsLocalDataSourceImpl
import com.mvproject.tinyiptvkmp.features.settings.api.data.repository.SettingsRepositoryImpl
import com.mvproject.tinyiptvkmp.features.settings.api.domain.repository.SettingsRepository
import com.mvproject.tinyiptvkmp.features.settings.api.domain.usecase.ObserveGeneralSettingsUseCase
import com.mvproject.tinyiptvkmp.features.settings.api.domain.usecase.ObserveGeneralSettingsUseCaseImpl
import com.mvproject.tinyiptvkmp.features.settings.api.domain.usecase.ObservePlayerSettingsUseCase
import com.mvproject.tinyiptvkmp.features.settings.api.domain.usecase.ObservePlayerSettingsUseCaseImpl
import com.mvproject.tinyiptvkmp.features.settings.api.domain.usecase.UpdateChannelsViewTypeUseCase
import com.mvproject.tinyiptvkmp.features.settings.api.domain.usecase.UpdateChannelsViewTypeUseCaseImpl
import com.mvproject.tinyiptvkmp.features.settings.api.domain.usecase.UpdateEpgUpdatePeriodUseCase
import com.mvproject.tinyiptvkmp.features.settings.api.domain.usecase.UpdateEpgUpdatePeriodUseCaseImpl
import com.mvproject.tinyiptvkmp.features.settings.api.domain.usecase.UpdateFullscreenModeUseCase
import com.mvproject.tinyiptvkmp.features.settings.api.domain.usecase.UpdateFullscreenModeUseCaseImpl
import com.mvproject.tinyiptvkmp.features.settings.api.domain.usecase.UpdateInfoUpdatePeriodUseCase
import com.mvproject.tinyiptvkmp.features.settings.api.domain.usecase.UpdateInfoUpdatePeriodUseCaseImpl
import com.mvproject.tinyiptvkmp.features.settings.api.domain.usecase.UpdateVideoSizeUseCase
import com.mvproject.tinyiptvkmp.features.settings.api.domain.usecase.UpdateVideoSizeUseCaseImpl
import org.koin.dsl.module

val settingsApiModule =
    module {
        single<SettingsLocalDataSource> { SettingsLocalDataSourceImpl(get()) }
        single<SettingsRepository> { SettingsRepositoryImpl(get()) }
        single<ObserveGeneralSettingsUseCase> { ObserveGeneralSettingsUseCaseImpl(get()) }
        single<ObservePlayerSettingsUseCase> { ObservePlayerSettingsUseCaseImpl(get()) }
        single<UpdateInfoUpdatePeriodUseCase> { UpdateInfoUpdatePeriodUseCaseImpl(get()) }
        single<UpdateEpgUpdatePeriodUseCase> { UpdateEpgUpdatePeriodUseCaseImpl(get()) }
        single<UpdateChannelsViewTypeUseCase> { UpdateChannelsViewTypeUseCaseImpl(get()) }
        single<UpdateFullscreenModeUseCase> { UpdateFullscreenModeUseCaseImpl(get()) }
        single<UpdateVideoSizeUseCase> { UpdateVideoSizeUseCaseImpl(get()) }
    }
