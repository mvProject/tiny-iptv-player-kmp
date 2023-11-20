/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.settings.player

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.ui.screens.settings.player.action.SettingsPlayerAction
import com.mvproject.tinyiptvkmp.ui.screens.settings.player.state.SettingsPlayerState
import com.mvproject.tinyiptvkmp.utils.KLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsPlayerViewModel(
    private val preferenceRepository: PreferenceRepository
) : ScreenModel {

    private val _settingsPlayerState = MutableStateFlow(SettingsPlayerState())
    val settingsPlayerState = _settingsPlayerState.asStateFlow()

    init {
        screenModelScope.launch {
            _settingsPlayerState.update {
                it.copy(
                    isFullscreenEnabled = preferenceRepository.getDefaultFullscreenMode(),
                    resizeMode = preferenceRepository.getDefaultResizeMode(),
                    ratioMode = preferenceRepository.getDefaultRatioMode()
                )
            }
        }
    }

    fun processAction(action: SettingsPlayerAction) {
        when (action) {
            is SettingsPlayerAction.SetFullScreenMode -> {
                val fullScreenState = action.state
                KLog.w("testing fullScreenState:$fullScreenState")
                screenModelScope.launch {
                    _settingsPlayerState.update {
                        it.copy(isFullscreenEnabled = fullScreenState)
                    }
                    preferenceRepository.setDefaultFullscreenMode(state = fullScreenState)
                }
            }

            is SettingsPlayerAction.SetResizeMode -> {
                val resizeMode = action.mode
                KLog.w("testing resizeMode:$resizeMode")
                screenModelScope.launch {
                    _settingsPlayerState.update {
                        it.copy(resizeMode = resizeMode)
                    }
                    preferenceRepository.setDefaultResizeMode(mode = resizeMode)
                }
            }

            is SettingsPlayerAction.SetRatioMode -> {
                val ratioMode = action.mode
                KLog.w("testing ratioMode:$ratioMode")
                screenModelScope.launch {
                    _settingsPlayerState.update {
                        it.copy(ratioMode = ratioMode)
                    }
                    preferenceRepository.setDefaultRatioMode(mode = ratioMode)
                }
            }
        }
    }
}