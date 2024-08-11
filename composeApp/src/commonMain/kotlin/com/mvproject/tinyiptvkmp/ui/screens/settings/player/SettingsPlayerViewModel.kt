/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.settings.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.ui.screens.settings.player.action.SettingsPlayerAction
import com.mvproject.tinyiptvkmp.ui.screens.settings.player.state.SettingsPlayerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsPlayerViewModel(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    private val _settingsPlayerState = MutableStateFlow(SettingsPlayerState())
    val settingsPlayerState = _settingsPlayerState.asStateFlow()

    init {
        viewModelScope.launch {
            _settingsPlayerState.update { current ->
                current.copy(
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
                viewModelScope.launch {
                    _settingsPlayerState.update { current ->
                        current.copy(isFullscreenEnabled = fullScreenState)
                    }
                    preferenceRepository.setDefaultFullscreenMode(state = fullScreenState)
                }
            }

            is SettingsPlayerAction.SetResizeMode -> {
                val resizeMode = action.mode
                viewModelScope.launch {
                    _settingsPlayerState.update { current ->
                        current.copy(resizeMode = resizeMode)
                    }
                    preferenceRepository.setDefaultResizeMode(mode = resizeMode)
                }
            }

            is SettingsPlayerAction.SetRatioMode -> {
                val ratioMode = action.mode
                viewModelScope.launch {
                    _settingsPlayerState.update { current ->
                        current.copy(ratioMode = ratioMode)
                    }
                    preferenceRepository.setDefaultRatioMode(mode = ratioMode)
                }
            }
        }
    }
}