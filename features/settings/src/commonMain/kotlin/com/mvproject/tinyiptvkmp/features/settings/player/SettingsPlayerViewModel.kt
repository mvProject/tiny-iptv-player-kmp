/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.features.settings.player

import com.mvproject.tinyiptvkmp.core.base.mvi.MviViewModel
import com.mvproject.tinyiptvkmp.features.player.api.domain.usecase.ObservePlayerSettingsUseCase
import com.mvproject.tinyiptvkmp.features.player.api.domain.usecase.UpdateFullscreenModeUseCase
import com.mvproject.tinyiptvkmp.features.player.api.domain.usecase.UpdateVideoSizeUseCase
import com.mvproject.tinyiptvkmp.features.settings.nav.SettingsNavigator
import com.mvproject.tinyiptvkmp.features.settings.player.SettingsPlayerState.SettingsPlayer
import org.koin.core.component.inject

class SettingsPlayerViewModel(
    private val observePlayerSettings: ObservePlayerSettingsUseCase,
    private val updateFullscreenMode: UpdateFullscreenModeUseCase,
    private val updateVideoSize: UpdateVideoSizeUseCase,
) : MviViewModel<SettingsPlayerState, SettingsPlayerAction, SettingsPlayerEffect>() {

    private val navigator: SettingsNavigator by inject()

    override fun createStore() = createStore(
        initialState = SettingsPlayerState(),
        invokeOnStart = { listenSettings() },
    )

    override fun onIntent(intent: SettingsPlayerAction) {
        when (intent) {
            SettingsPlayerAction.NavigateBack -> launch { navigator.navigateUp() }
            is SettingsPlayerAction.SetFullScreenMode -> launch { setFullscreenMode(state = intent.state) }
            is SettingsPlayerAction.SetVideoSize -> launch { setVideoSizeMode(mode = intent.mode) }
            is SettingsPlayerAction.ToggleOption -> toggleOption(type = intent.type)
        }
    }

    private suspend fun listenSettings() {
        observePlayerSettings().collect { settings ->
            setState {
                copy(
                    isFullscreenEnabled = settings.isFullscreenEnabled,
                    videoSize = settings.videoSize,
                )
            }
        }
    }

    private fun toggleOption(type: SettingsPlayer) {
        val settingsType = if (state.value.settingsType == type) null else type
        setState {
            copy(settingsType = settingsType)
        }
    }

    private suspend fun setFullscreenMode(state: Boolean) {
        updateFullscreenMode(state)
    }

    private suspend fun setVideoSizeMode(mode: Int) {
        updateVideoSize(mode)
        setState {
            copy(settingsType = null)
        }
    }
}
