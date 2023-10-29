/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 31.05.23, 17:08
 *
 */

package com.mvproject.tinyiptv.ui.screens.settings.general

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.mvproject.tinyiptv.data.repository.PreferenceRepository
import com.mvproject.tinyiptv.ui.screens.settings.general.action.SettingsAction
import com.mvproject.tinyiptv.ui.screens.settings.general.state.SettingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferenceRepository: PreferenceRepository
) : ScreenModel {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        coroutineScope.launch {
            _state.update {
                it.copy(
                    infoUpdatePeriod = preferenceRepository.getEpgInfoUpdatePeriod(),
                    epgUpdatePeriod = preferenceRepository.getMainEpgUpdatePeriod(),
                )
            }
        }
    }

    fun processAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.SetInfoUpdatePeriod -> {
                val newType = action.type
                coroutineScope.launch {
                    _state.update {
                        it.copy(infoUpdatePeriod = newType)
                    }
                    preferenceRepository.setEpgInfoUpdatePeriod(type = newType)
                }
            }

            is SettingsAction.SetEpgUpdatePeriod -> {
                val newType = action.type
                coroutineScope.launch {
                    _state.update {
                        it.copy(epgUpdatePeriod = newType)
                    }
                    preferenceRepository.setMainEpgUpdatePeriod(type = newType)
                }
            }
        }
    }
}