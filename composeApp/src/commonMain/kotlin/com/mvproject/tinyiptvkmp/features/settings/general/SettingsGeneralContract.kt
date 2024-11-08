package com.mvproject.tinyiptvkmp.features.settings.general

import androidx.compose.runtime.Stable
import com.mvproject.tinyiptvkmp.core.common.AppConstants

interface SettingsGeneralContract {
    @Stable
    data class UiState(
        val infoUpdatePeriod: Int = AppConstants.INT_VALUE_ZERO,
        val epgUpdatePeriod: Int = AppConstants.INT_VALUE_ZERO,
    )

    sealed interface UiAction {
        data class SetInfoUpdatePeriod(val type: Int) : UiAction
        data class SetEpgUpdatePeriod(val type: Int) : UiAction
        data object NavigateBack : UiAction
        data object NavigateToPlayerSettings : UiAction
        data object NavigateToPlaylistSettings : UiAction
    }

    sealed interface UiEffect {
        data object NavigateBack : UiEffect
        data object NavigateToPlayerSettings : UiEffect
        data object NavigateToPlaylistSettings : UiEffect
    }
}