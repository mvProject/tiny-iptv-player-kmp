/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.09.23, 18:00
 *
 */

package com.mvproject.tinyiptv.ui.screens.settings.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptv.data.helpers.PlaylistHelper
import com.mvproject.tinyiptv.data.usecases.DeletePlaylistUseCase
import com.mvproject.tinyiptv.ui.screens.settings.playlist.action.SettingsPlaylistAction
import com.mvproject.tinyiptv.ui.screens.settings.playlist.state.SettingsPlaylistState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsPlaylistViewModel(
    private val playlistHelper: PlaylistHelper,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
) : ViewModel() {

    private val _playlistDataState = MutableStateFlow(SettingsPlaylistState())
    val playlistDataState = _playlistDataState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            playlistHelper.allPlaylistsFlow.collect {
                _playlistDataState.update { state ->
                    state.copy(
                        playlists = playlistHelper.loadPlaylists(),
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