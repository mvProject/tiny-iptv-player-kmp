/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.features.settings.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptvkmp.core.common.mvi.MVI
import com.mvproject.tinyiptvkmp.core.common.mvi.mvi
import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.features.settings.player.SettingsPlayerContract.UiAction
import com.mvproject.tinyiptvkmp.features.settings.player.SettingsPlayerContract.UiEffect
import com.mvproject.tinyiptvkmp.features.settings.player.SettingsPlayerContract.UiState
import kotlinx.coroutines.launch

class SettingsPlayerViewModel(
    private val preferenceRepository: PreferenceRepository
) : ViewModel(),
    MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    init {
        viewModelScope.launch {
            val isFullscreenEnabled = preferenceRepository.getDefaultFullscreenMode()
            val resizeMode = preferenceRepository.getDefaultResizeMode()
            val ratioMode = preferenceRepository.getDefaultRatioMode()

            updateUiState {
                copy(
                    isFullscreenEnabled = isFullscreenEnabled,
                    resizeMode = resizeMode,
                    ratioMode = ratioMode
                )
            }
        }
    }

    override fun onAction(uiAction: UiAction) {
        when (uiAction) {
            UiAction.NavigateBack -> viewModelScope.postUiEffect(UiEffect.NavigateBack)
            is UiAction.SetFullScreenMode -> setFullscreenMode(state = uiAction.state)
            is UiAction.SetRatioMode -> setRatioMode(mode = uiAction.mode)
            is UiAction.SetResizeMode -> setResizeMode(mode = uiAction.mode)
        }
    }

    private fun setFullscreenMode(state: Boolean) {
        viewModelScope.launch {
            preferenceRepository.setDefaultFullscreenMode(state = state)
            updateUiState {
                copy(isFullscreenEnabled = state)
            }
        }
    }

    private fun setResizeMode(mode: Int) {
        viewModelScope.launch {
            preferenceRepository.setDefaultResizeMode(mode = mode)
            updateUiState {
                copy(resizeMode = mode)
            }
        }
    }

    private fun setRatioMode(mode: Int) {
        viewModelScope.launch {
            preferenceRepository.setDefaultRatioMode(mode = mode)
            updateUiState {
                copy(ratioMode = mode)
            }
        }
    }
}