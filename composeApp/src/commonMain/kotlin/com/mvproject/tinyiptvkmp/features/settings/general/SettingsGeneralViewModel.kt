/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.features.settings.general

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptvkmp.core.base.mvi.MviCore
import com.mvproject.tinyiptvkmp.core.base.mvi.mviCore
import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import com.mvproject.tinyiptvkmp.core.datastore.preferences.AppPreferencesProto
import com.mvproject.tinyiptvkmp.core.foundation.common.INT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.features.settings.general.SettingsGeneralUiState.SettingsGeneral
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsGeneralViewModel(
    private val preferencesStore: ProtoStore<AppPreferencesProto>,
) : ViewModel(),
    MviCore<SettingsGeneralUiState, SettingsGeneralUiAction, SettingsGeneralUiEffect> by mviCore(
        SettingsGeneralUiState()
    ) {

    init {
        viewModelScope.launch {
            val preferences = preferencesStore.data.first()
            updateUiState {
                copy(
                    infoUpdatePeriod = preferences.epgInfoLastUpdatePeriod,
                    epgUpdatePeriod = preferences.epgMainLastUpdatePeriod
                )
            }
        }
    }

    override fun onAction(uiAction: SettingsGeneralUiAction) {
        when (uiAction) {
            SettingsGeneralUiAction.NavigateBack -> viewModelScope.postUiEffect(
                SettingsGeneralUiEffect.OnNavigateBack
            )

            SettingsGeneralUiAction.NavigateToPlayerSettings -> viewModelScope.postUiEffect(
                SettingsGeneralUiEffect.OnNavigateToPlayerSettings
            )

            SettingsGeneralUiAction.NavigateToPlaylistSettings -> viewModelScope.postUiEffect(
                SettingsGeneralUiEffect.OnNavigateToPlaylistSettings
            )

            is SettingsGeneralUiAction.SetEpgUpdatePeriod -> setUpdateEpgProgramsPeriod(type = uiAction.type)
            is SettingsGeneralUiAction.SetInfoUpdatePeriod -> setUpdateInfoPeriod(type = uiAction.type)
            is SettingsGeneralUiAction.ToggleOption -> toggleOption(type = uiAction.type)
        }
    }

    private fun toggleOption(type: SettingsGeneral) {
        val settingsType = if (uiState.value.settingsType == type) null else type
        updateUiState {
            copy(settingsType = settingsType)
        }
    }

    private fun setUpdateInfoPeriod(type: Int) {
        viewModelScope.launch {
            preferencesStore.update { preferences ->
                preferences.copy(epgInfoLastUpdatePeriod = type)
            }
            updateUiState {
                copy(infoUpdatePeriod = type, settingsType = null)
            }
        }
    }

    private fun setUpdateEpgProgramsPeriod(type: Int) {
        viewModelScope.launch {
            preferencesStore.update { preferences ->
                preferences.copy(epgMainLastUpdatePeriod = type)
            }
            updateUiState {
                copy(epgUpdatePeriod = type, settingsType = null)
            }
        }
    }
}

@Immutable
data class SettingsGeneralUiState(
    val infoUpdatePeriod: Int = INT_VALUE_ZERO,
    val epgUpdatePeriod: Int = INT_VALUE_ZERO,
    val settingsType: SettingsGeneral? = null,
) {
    sealed interface SettingsGeneral {
        data object InfoUpdate : SettingsGeneral
        data object ProgramsUpdate : SettingsGeneral
    }
}

sealed interface SettingsGeneralUiAction {
    data class SetInfoUpdatePeriod(val type: Int) : SettingsGeneralUiAction
    data class SetEpgUpdatePeriod(val type: Int) : SettingsGeneralUiAction
    data class ToggleOption(val type: SettingsGeneral) : SettingsGeneralUiAction
    data object NavigateBack : SettingsGeneralUiAction
    data object NavigateToPlayerSettings : SettingsGeneralUiAction
    data object NavigateToPlaylistSettings : SettingsGeneralUiAction
}

sealed interface SettingsGeneralUiEffect {
    data object OnNavigateBack : SettingsGeneralUiEffect
    data object OnNavigateToPlayerSettings : SettingsGeneralUiEffect
    data object OnNavigateToPlaylistSettings : SettingsGeneralUiEffect
}
