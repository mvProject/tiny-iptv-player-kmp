/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.features.settings.presentation.general

import com.mvproject.tinyiptvkmp.core.base.mvi.MviViewModel
import com.mvproject.tinyiptvkmp.core.navigation.NavigationOptions
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.ObserveEpgSettingsUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.UpdateEpgUpdatePeriodUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.UpdateInfoUpdatePeriodUseCase
import com.mvproject.tinyiptvkmp.features.settings.presentation.general.SettingsGeneralState.SettingsGeneral
import com.mvproject.tinyiptvkmp.features.settings.presentation.nav.SettingsNavigator
import org.koin.core.component.inject

class SettingsGeneralViewModel(
    private val observeEpgSettings: ObserveEpgSettingsUseCase,
    private val updateInfoUpdatePeriod: UpdateInfoUpdatePeriodUseCase,
    private val updateEpgUpdatePeriod: UpdateEpgUpdatePeriodUseCase,
) : MviViewModel<SettingsGeneralState, SettingsGeneralAction, SettingsGeneralEffect>() {

    private val navigator: SettingsNavigator by inject()

    override fun createStore() = createStore(
        initialState = SettingsGeneralState(),
        invokeOnStart = { listenSettings() },
    )

    override fun onIntent(intent: SettingsGeneralAction) {
        when (intent) {
            SettingsGeneralAction.NavigateBack -> launch { navigator.navigateUp() }
            SettingsGeneralAction.NavigateToPlayerSettings -> launch {
                navigator.navigateToPlayerSettings(options = settingsNavigationOptions)
            }

            SettingsGeneralAction.NavigateToPlaylistSettings -> launch {
                navigator.navigateToPlaylistSettings(options = settingsNavigationOptions)
            }

            is SettingsGeneralAction.SetEpgUpdatePeriod -> launch { setUpdateEpgProgramsPeriod(type = intent.type) }
            is SettingsGeneralAction.SetInfoUpdatePeriod -> launch { setUpdateInfoPeriod(type = intent.type) }
            is SettingsGeneralAction.ToggleOption -> toggleOption(type = intent.type)
        }
    }

    private suspend fun listenSettings() {
        observeEpgSettings().collect { settings ->
            setState {
                copy(
                    infoUpdatePeriod = settings.epgInfoUpdatePeriod,
                    epgUpdatePeriod = settings.epgUpdatePeriod,
                )
            }
        }
    }

    private fun toggleOption(type: SettingsGeneral) {
        val settingsType = if (state.value.settingsType == type) null else type
        setState {
            copy(settingsType = settingsType)
        }
    }

    private suspend fun setUpdateInfoPeriod(type: Int) {
        updateInfoUpdatePeriod(type)
        setState {
            copy(settingsType = null)
        }
    }

    private suspend fun setUpdateEpgProgramsPeriod(type: Int) {
        updateEpgUpdatePeriod(type)
        setState {
            copy(settingsType = null)
        }
    }
}

private val settingsNavigationOptions = NavigationOptions(launchSingleTop = true)
