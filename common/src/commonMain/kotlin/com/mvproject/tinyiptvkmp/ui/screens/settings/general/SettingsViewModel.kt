/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.settings.general

import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.ui.screens.settings.general.action.SettingsAction
import com.mvproject.tinyiptvkmp.ui.screens.settings.general.state.SettingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class SettingsViewModel(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    infoUpdatePeriod = preferenceRepository.getEpgInfoUpdatePeriod(),
                    epgUpdatePeriod = preferenceRepository.getMainEpgUpdatePeriod()
                )
            }
        }
    }

    fun processAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.SetInfoUpdatePeriod -> {
                val newType = action.type
                viewModelScope.launch {
                    _state.update { current ->
                        current.copy(infoUpdatePeriod = newType)
                    }
                    preferenceRepository.setEpgInfoUpdatePeriod(type = newType)
                }
            }

            is SettingsAction.SetEpgUpdatePeriod -> {
                val newType = action.type
                viewModelScope.launch {
                    _state.update { current ->
                        current.copy(epgUpdatePeriod = newType)
                    }
                    preferenceRepository.setMainEpgUpdatePeriod(type = newType)
                }
            }
        }
    }
}