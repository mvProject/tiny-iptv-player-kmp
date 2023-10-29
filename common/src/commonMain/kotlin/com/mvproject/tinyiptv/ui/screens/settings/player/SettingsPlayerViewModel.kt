/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 27.10.23, 21:28
 *
 */

package com.mvproject.tinyiptv.ui.screens.settings.player

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.mvproject.tinyiptv.data.repository.PreferenceRepository
import com.mvproject.tinyiptv.ui.screens.settings.player.action.SettingsPlayerAction
import com.mvproject.tinyiptv.ui.screens.settings.player.state.SettingsPlayerState
import io.github.aakira.napier.Napier
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
        coroutineScope.launch {
            _settingsPlayerState.update {
                it.copy(
                    isFullscreenEnabled = preferenceRepository.getDefaultFullscreenMode(),
                    resizeMode = preferenceRepository.getDefaultResizeMode()
                )
            }
        }
    }

    fun processAction(action: SettingsPlayerAction) {
        when (action) {
            is SettingsPlayerAction.SetFullScreenMode -> {
                val fullScreenState = action.state
                Napier.w("testing fullScreenState:$fullScreenState")
                coroutineScope.launch {
                    _settingsPlayerState.update {
                        it.copy(isFullscreenEnabled = fullScreenState)
                    }
                    preferenceRepository.setDefaultFullscreenMode(state = fullScreenState)
                }
            }

            is SettingsPlayerAction.SetResizeMode -> {
                val resizeMode = action.mode
                Napier.w("testing resizeMode:$resizeMode")
                coroutineScope.launch {
                    _settingsPlayerState.update {
                        it.copy(resizeMode = resizeMode)
                    }
                    preferenceRepository.setDefaultResizeMode(mode = resizeMode)
                }
            }
        }
    }
}