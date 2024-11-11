/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.features.settings.player

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptvkmp.core.common.mvi.MviCore
import com.mvproject.tinyiptvkmp.core.common.mvi.mviCore
import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.core.domain.enums.RatioMode
import com.mvproject.tinyiptvkmp.core.domain.enums.ResizeMode
import kotlinx.coroutines.launch

class SettingsPlayerViewModel(
    private val preferenceRepository: PreferenceRepository
) : ViewModel(),
    MviCore<SettingsPlayerUiState, SettingsPlayerUiAction, SettingsPlayerUiEffect> by mviCore(
        SettingsPlayerUiState()
    ) {

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

    override fun onAction(uiAction: SettingsPlayerUiAction) {
        when (uiAction) {
            SettingsPlayerUiAction.NavigateBack -> viewModelScope.postUiEffect(
                SettingsPlayerUiEffect.OnNavigateBack
            )

            is SettingsPlayerUiAction.SetFullScreenMode -> setFullscreenMode(state = uiAction.state)
            is SettingsPlayerUiAction.SetRatioMode -> setRatioMode(mode = uiAction.mode)
            is SettingsPlayerUiAction.SetResizeMode -> setResizeMode(mode = uiAction.mode)
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

@Immutable
data class SettingsPlayerUiState(
    val resizeMode: Int = ResizeMode.Fill.value,
    val ratioMode: Int = RatioMode.WideScreen.value,
    val isFullscreenEnabled: Boolean = true,
)

sealed interface SettingsPlayerUiAction {
    data class SetResizeMode(val mode: Int) : SettingsPlayerUiAction
    data class SetRatioMode(val mode: Int) : SettingsPlayerUiAction
    data class SetFullScreenMode(val state: Boolean) : SettingsPlayerUiAction
    data object NavigateBack : SettingsPlayerUiAction

}

sealed interface SettingsPlayerUiEffect {
    data object OnNavigateBack : SettingsPlayerUiEffect
}