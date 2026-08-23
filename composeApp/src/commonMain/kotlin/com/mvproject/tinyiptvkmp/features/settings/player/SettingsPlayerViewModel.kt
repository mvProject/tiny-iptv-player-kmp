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
import com.mvproject.tinyiptvkmp.core.base.mvi.MviCore
import com.mvproject.tinyiptvkmp.core.base.mvi.mviCore
import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import com.mvproject.tinyiptvkmp.core.datastore.preferences.AppPreferencesProto
import com.mvproject.tinyiptvkmp.core.domain.enums.VideoSize
import com.mvproject.tinyiptvkmp.features.settings.player.SettingsPlayerUiState.SettingsPlayer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsPlayerViewModel(
    private val preferencesStore: ProtoStore<AppPreferencesProto>,
) : ViewModel(),
    MviCore<SettingsPlayerUiState, SettingsPlayerUiAction, SettingsPlayerUiEffect> by mviCore(
        SettingsPlayerUiState()
    ) {

    init {
        viewModelScope.launch {
            val preferences = preferencesStore.data.first()

            updateUiState {
                copy(
                    isFullscreenEnabled = preferences.defaultFullscreenMode,
                    videoSize = preferences.defaultVideoSizeMode
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
            is SettingsPlayerUiAction.ToggleOption -> toggleOption(type = uiAction.type)
            is SettingsPlayerUiAction.SetVideoSize -> setVideoSizeMode(mode = uiAction.mode)
        }
    }

    private fun toggleOption(type: SettingsPlayer) {
        val settingsType = if (uiState.value.settingsType == type) null else type
        updateUiState {
            copy(settingsType = settingsType)
        }
    }

    private fun setFullscreenMode(state: Boolean) {
        viewModelScope.launch {
            preferencesStore.update { preferences ->
                preferences.copy(defaultFullscreenMode = state)
            }
            updateUiState {
                copy(isFullscreenEnabled = state)
            }
        }
    }

    private fun setVideoSizeMode(mode: Int) {
        viewModelScope.launch {
            preferencesStore.update { preferences ->
                preferences.copy(defaultVideoSizeMode = mode)
            }
            updateUiState {
                copy(videoSize = mode, settingsType = null)
            }
        }
    }
}

@Immutable
data class SettingsPlayerUiState(
    val videoSize: Int = VideoSize.WideScreen.ordinal,
    val isFullscreenEnabled: Boolean = true,
    val settingsType: SettingsPlayer? = null,
) {
    sealed interface SettingsPlayer {
        data object VideoSize : SettingsPlayer
    }
}

sealed interface SettingsPlayerUiAction {
    data class SetVideoSize(val mode: Int) : SettingsPlayerUiAction
    data class SetFullScreenMode(val state: Boolean) : SettingsPlayerUiAction
    data class ToggleOption(val type: SettingsPlayer) : SettingsPlayerUiAction
    data object NavigateBack : SettingsPlayerUiAction

}

sealed interface SettingsPlayerUiEffect {
    data object OnNavigateBack : SettingsPlayerUiEffect
}
