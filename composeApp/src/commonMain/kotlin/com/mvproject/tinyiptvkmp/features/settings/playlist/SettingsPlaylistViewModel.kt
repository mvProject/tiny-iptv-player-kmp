/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 17:25
 *
 */

package com.mvproject.tinyiptvkmp.features.settings.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptvkmp.core.common.mvi.MVI
import com.mvproject.tinyiptvkmp.core.common.mvi.mvi
import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.core.domain.model.Playlist
import com.mvproject.tinyiptvkmp.core.domain.usecase.DeletePlaylistUseCase
import com.mvproject.tinyiptvkmp.features.settings.playlist.SettingsPlaylistContract.UiAction
import com.mvproject.tinyiptvkmp.features.settings.playlist.SettingsPlaylistContract.UiEffect
import com.mvproject.tinyiptvkmp.features.settings.playlist.SettingsPlaylistContract.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SettingsPlaylistViewModel(
    private val playlistsRepository: PlaylistsRepository,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
) : ViewModel(),
    MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    init {
        playlistsRepository
            .allPlaylistsAsFlow()
            .onStart {
                updateUiState {
                    copy(isLoading = true)
                }
            }
            .flowOn(Dispatchers.IO)
            .onEach { lists ->

                val playlistState = if (lists.isEmpty())
                    PlaylistState.Empty
                else
                    PlaylistState.Success(lists)

                updateUiState {
                    copy(
                        playlistState = playlistState,
                        isLoading = false,
                    )
                }
            }.launchIn(viewModelScope)
    }

    override fun onAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.DeletePlaylist -> deletePlaylist(playlist = uiAction.playlist)
            UiAction.NavigateBack -> viewModelScope.postUiEffect(UiEffect.NavigateBack)
            is UiAction.NavigateToPlaylist ->
                viewModelScope.postUiEffect(UiEffect.NavigateToPlaylist(uiAction.id))
        }
    }

    private fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            deletePlaylistUseCase(playlist = playlist)
        }
    }
}
