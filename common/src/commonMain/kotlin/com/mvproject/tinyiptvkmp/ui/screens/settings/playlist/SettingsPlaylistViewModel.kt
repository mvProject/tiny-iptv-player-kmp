/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.settings.playlist

import com.mvproject.tinyiptvkmp.data.helpers.PlaylistHelper
import com.mvproject.tinyiptvkmp.data.usecases.DeletePlaylistUseCase
import com.mvproject.tinyiptvkmp.ui.screens.settings.playlist.action.SettingsPlaylistAction
import com.mvproject.tinyiptvkmp.ui.screens.settings.playlist.state.SettingsPlaylistState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class SettingsPlaylistViewModel(
    private val playlistHelper: PlaylistHelper,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
) : ViewModel() {

    private val _playlistDataState = MutableStateFlow(SettingsPlaylistState())
    val playlistDataState = _playlistDataState.asStateFlow()

    init {
        viewModelScope.launch {
            playlistHelper.allPlaylistsFlow.collect { lists ->
                _playlistDataState.update { current ->
                    current.copy(
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
                viewModelScope.launch {
                    deletePlaylistUseCase(playlist = action.playlist)
                }
            }
        }
    }
}