/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 17:25
 *
 */

package com.mvproject.tinyiptvkmp.features.settings.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.core.domain.usecase.DeletePlaylistUseCase
import com.mvproject.tinyiptvkmp.features.settings.playlist.action.SettingsPlaylistAction
import com.mvproject.tinyiptvkmp.features.settings.playlist.state.SettingsPlaylistState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
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
        playlistsRepository
            .allPlaylistsAsFlow()
            .flowOn(Dispatchers.IO)
            .onEach { lists ->
                _playlistDataState.update { current ->
                    current.copy(
                        playlists = lists,
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
