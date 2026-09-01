package com.mvproject.tinyiptvkmp.features.settings.presentation.player

import androidx.compose.runtime.Immutable
import com.mvproject.tinyiptvkmp.core.foundation.model.VideoSize


@Immutable
data class SettingsPlayerState(
    val videoSize: Int = VideoSize.WideScreen.ordinal,
    val isFullscreenEnabled: Boolean = true,
    val settingsType: SettingsPlayer? = null,
) {
    sealed interface SettingsPlayer {
        data object VideoSize : SettingsPlayer
    }
}

sealed interface SettingsPlayerAction {
    data class SetVideoSize(val mode: Int) : SettingsPlayerAction
    data class SetFullScreenMode(val state: Boolean) : SettingsPlayerAction
    data class ToggleOption(val type: SettingsPlayerState.SettingsPlayer) : SettingsPlayerAction
    data object NavigateBack : SettingsPlayerAction

}

sealed interface SettingsPlayerEffect