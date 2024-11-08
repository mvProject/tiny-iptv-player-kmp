/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.features.settings.general

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptvkmp.core.common.mvi.MVI
import com.mvproject.tinyiptvkmp.core.common.mvi.mvi
import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.features.settings.general.SettingsGeneralContract.UiAction
import com.mvproject.tinyiptvkmp.features.settings.general.SettingsGeneralContract.UiEffect
import com.mvproject.tinyiptvkmp.features.settings.general.SettingsGeneralContract.UiState
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferenceRepository: PreferenceRepository
) : ViewModel(),
    MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

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

    override fun onAction(uiAction: UiAction) {
        when (uiAction) {
            UiAction.NavigateBack -> viewModelScope.postUiEffect(UiEffect.NavigateBack)
            UiAction.NavigateToPlayerSettings -> viewModelScope.postUiEffect(UiEffect.NavigateToPlayerSettings)
            UiAction.NavigateToPlaylistSettings -> viewModelScope.postUiEffect(UiEffect.NavigateToPlaylistSettings)
            is UiAction.SetEpgUpdatePeriod -> setUpdateEpgProgramsPeriod(type = uiAction.type)
            is UiAction.SetInfoUpdatePeriod -> setUpdateInfoPeriod(type = uiAction.type)
        }
    }

    private fun setUpdateInfoPeriod(type: Int) {
        viewModelScope.launch {
            preferenceRepository.setEpgInfoUpdatePeriod(type = type)
            updateUiState {
                copy(infoUpdatePeriod = type)
            }
        }
    }

    private fun setUpdateEpgProgramsPeriod(type: Int) {
        viewModelScope.launch {
            preferenceRepository.setMainEpgUpdatePeriod(type = type)
            updateUiState {
                copy(epgUpdatePeriod = type)
            }
        }
    }
}