/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.settings.playlist

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.mvproject.tinyiptvkmp.data.helpers.PlaylistHelper
import com.mvproject.tinyiptvkmp.data.usecases.DeletePlaylistUseCase
import com.mvproject.tinyiptvkmp.ui.screens.settings.playlist.action.SettingsPlaylistAction
import com.mvproject.tinyiptvkmp.ui.screens.settings.playlist.state.SettingsPlaylistState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsPlaylistViewModel(
    private val playlistHelper: PlaylistHelper,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
) : ScreenModel {

    private val _playlistDataState = MutableStateFlow(SettingsPlaylistState())
    val playlistDataState = _playlistDataState.asStateFlow()

    init {
        screenModelScope.launch(Dispatchers.IO) {
            playlistHelper.allPlaylistsFlow.collect { lists ->
                _playlistDataState.update { state ->
                    state.copy(
                        playlists = lists,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun processAction(action: SettingsPlaylistAction) {
        when (action) {
            is SettingsPlaylistAction.DeletePlaylist -> {
                screenModelScope.launch {
                    deletePlaylistUseCase(playlist = action.playlist)
                }
            }
        }
    }
}