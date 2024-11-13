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
import com.mvproject.tinyiptvkmp.core.common.AppConstants
import com.mvproject.tinyiptvkmp.core.common.mvi.MviCore
import com.mvproject.tinyiptvkmp.core.common.mvi.mviCore
import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.features.settings.general.SettingsGeneralUiState.OsdType
import kotlinx.coroutines.launch

class SettingsGeneralViewModel(
    private val preferenceRepository: PreferenceRepository
) : ViewModel(),
    MviCore<SettingsGeneralUiState, SettingsGeneralUiAction, SettingsGeneralUiEffect> by mviCore(
        SettingsGeneralUiState()
    ) {

    init {
        viewModelScope.launch {
            val infoUpdatePeriod = preferenceRepository.getEpgInfoUpdatePeriod()
            val epgUpdatePeriod = preferenceRepository.getMainEpgUpdatePeriod()
            updateUiState {
                copy(
                    infoUpdatePeriod = infoUpdatePeriod,
                    epgUpdatePeriod = epgUpdatePeriod
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
            SettingsGeneralUiAction.CloseOsd -> closeOsd()
            is SettingsGeneralUiAction.OpenOsd -> openOsd(type = uiAction.type)
        }
    }

    private fun openOsd(type: OsdType) {
        updateUiState {
            copy(osdType = type)
        }
    }

    private fun closeOsd() {
        updateUiState {
            copy(osdType = null)
        }
    }

    private fun setUpdateInfoPeriod(type: Int) {
        viewModelScope.launch {
            preferenceRepository.setEpgInfoUpdatePeriod(type = type)
            updateUiState {
                copy(infoUpdatePeriod = type, osdType = null)
            }
        }
    }

    private fun setUpdateEpgProgramsPeriod(type: Int) {
        viewModelScope.launch {
            preferenceRepository.setMainEpgUpdatePeriod(type = type)
            updateUiState {
                copy(epgUpdatePeriod = type, osdType = null)
            }
        }
    }
}

@Immutable
data class SettingsGeneralUiState(
    val infoUpdatePeriod: Int = AppConstants.INT_VALUE_ZERO,
    val epgUpdatePeriod: Int = AppConstants.INT_VALUE_ZERO,
    val osdType: OsdType? = null,
) {
    sealed interface OsdType {
        data object InfoUpdate : OsdType
        data object ProgramsUpdate : OsdType
    }
}

sealed interface SettingsGeneralUiAction {
    data class SetInfoUpdatePeriod(val type: Int) : SettingsGeneralUiAction
    data class SetEpgUpdatePeriod(val type: Int) : SettingsGeneralUiAction
    data class OpenOsd(val type: OsdType) : SettingsGeneralUiAction
    data object CloseOsd : SettingsGeneralUiAction
    data object NavigateBack : SettingsGeneralUiAction
    data object NavigateToPlayerSettings : SettingsGeneralUiAction
    data object NavigateToPlaylistSettings : SettingsGeneralUiAction
}

sealed interface SettingsGeneralUiEffect {
    data object OnNavigateBack : SettingsGeneralUiEffect
    data object OnNavigateToPlayerSettings : SettingsGeneralUiEffect
    data object OnNavigateToPlaylistSettings : SettingsGeneralUiEffect
}