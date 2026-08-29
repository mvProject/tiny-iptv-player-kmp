/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.features.settings.general

import com.mvproject.tinyiptvkmp.core.base.mvi.MviViewModel
import com.mvproject.tinyiptvkmp.features.settings.api.domain.usecase.ObserveGeneralSettingsUseCase
import com.mvproject.tinyiptvkmp.features.settings.api.domain.usecase.UpdateEpgUpdatePeriodUseCase
import com.mvproject.tinyiptvkmp.features.settings.api.domain.usecase.UpdateInfoUpdatePeriodUseCase
import com.mvproject.tinyiptvkmp.features.settings.general.SettingsGeneralState.SettingsGeneral
import com.mvproject.tinyiptvkmp.features.settings.nav.SettingsNavigator
import org.koin.core.component.inject

class SettingsGeneralViewModel(
    private val observeGeneralSettings: ObserveGeneralSettingsUseCase,
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
            SettingsGeneralAction.NavigateToPlayerSettings -> launch { navigator.navigateToPlayerSettings() }
            SettingsGeneralAction.NavigateToPlaylistSettings -> launch { navigator.navigateToPlaylistSettings() }
            is SettingsGeneralAction.SetEpgUpdatePeriod -> launch { setUpdateEpgProgramsPeriod(type = intent.type) }
            is SettingsGeneralAction.SetInfoUpdatePeriod -> launch { setUpdateInfoPeriod(type = intent.type) }
            is SettingsGeneralAction.ToggleOption -> toggleOption(type = intent.type)
        }
    }

    private suspend fun listenSettings() {
        observeGeneralSettings().collect { settings ->
            setState {
                copy(
                    infoUpdatePeriod = settings.infoUpdatePeriod,
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
