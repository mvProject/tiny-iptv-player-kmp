package com.mvproject.tinyiptvkmp.features.settings.player

import androidx.compose.runtime.Stable
import com.mvproject.tinyiptvkmp.core.domain.enums.RatioMode
import com.mvproject.tinyiptvkmp.core.domain.enums.ResizeMode

interface SettingsPlayerContract {
    @Stable
    data class UiState(
        val resizeMode: Int = ResizeMode.Fill.value,
        val ratioMode: Int = RatioMode.WideScreen.value,
        val isFullscreenEnabled: Boolean = true,
    )

    sealed interface UiAction {
        data class SetResizeMode(val mode: Int) : UiAction
        data class SetRatioMode(val mode: Int) : UiAction
        data class SetFullScreenMode(val state: Boolean) : UiAction
        data object NavigateBack : UiAction

    }

    sealed interface UiEffect {
        data object NavigateBack : UiEffect
    }
}