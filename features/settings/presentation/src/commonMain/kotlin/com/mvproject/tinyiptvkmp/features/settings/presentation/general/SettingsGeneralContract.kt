package com.mvproject.tinyiptvkmp.features.settings.presentation.general

import androidx.compose.runtime.Immutable
import com.mvproject.tinyiptvkmp.core.foundation.common.INT_VALUE_ZERO

@Immutable
data class SettingsGeneralState(
    val infoUpdatePeriod: Int = INT_VALUE_ZERO,
    val epgUpdatePeriod: Int = INT_VALUE_ZERO,
    val settingsType: SettingsGeneral? = null,
) {
    sealed interface SettingsGeneral {
        data object InfoUpdate : SettingsGeneral
        data object ProgramsUpdate : SettingsGeneral
    }
}

sealed interface SettingsGeneralAction {
    data class SetInfoUpdatePeriod(val type: Int) : SettingsGeneralAction
    data class SetEpgUpdatePeriod(val type: Int) : SettingsGeneralAction
    data class ToggleOption(val type: SettingsGeneralState.SettingsGeneral) : SettingsGeneralAction
    data object NavigateBack : SettingsGeneralAction
    data object NavigateToPlayerSettings : SettingsGeneralAction
    data object NavigateToPlaylistSettings : SettingsGeneralAction
}

sealed interface SettingsGeneralEffect