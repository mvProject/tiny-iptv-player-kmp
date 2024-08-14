/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 17:25
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.settings.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptvkmp.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.data.usecases.DeletePlaylistUseCase
import com.mvproject.tinyiptvkmp.ui.screens.settings.playlist.action.SettingsPlaylistAction
import com.mvproject.tinyiptvkmp.ui.screens.settings.playlist.state.Playlists
import com.mvproject.tinyiptvkmp.ui.screens.settings.playlist.state.SettingsPlaylistState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsPlaylistViewModel(
    private val playlistsRepository: PlaylistsRepository,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
) : ViewModel() {
    private val _playlistDataState = MutableStateFlow(SettingsPlaylistState())
    val playlistDataState = _playlistDataState.asStateFlow()

    init {
/*        viewModelScope.launch {
            playlistsRepository.allPlaylistsAsFlow().collect { lists ->
                _playlistDataState.update { current ->
                    current.copy(
                        playlists = Playlists(items = lists),
                        isLoading = false,
                    )
                }
            }
        }*/

        playlistsRepository
            .allPlaylistsAsFlow()
            .onEach { lists ->
                _playlistDataState.update { current ->
                    current.copy(
                        playlists = Playlists(items = lists),
                        isLoading = false,
                    )
                }
            }.launchIn(viewModelScope)
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
