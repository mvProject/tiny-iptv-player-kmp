/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 17:25
 *
 */

package com.mvproject.tinyiptvkmp.features.settings.playlist

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptvkmp.core.base.mvi.MviCore
import com.mvproject.tinyiptvkmp.core.base.mvi.mviCore
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.core.domain.model.Playlist
import com.mvproject.tinyiptvkmp.core.domain.usecase.DeletePlaylistUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SettingsPlaylistViewModel(
    private val playlistsRepository: PlaylistsRepository,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
) : ViewModel(),
    MviCore<SettingsPlaylistUiState, SettingsPlaylistUiAction, SettingsPlaylistUiEffect> by mviCore(
        SettingsPlaylistUiState()
    ) {

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
                    SettingsPlaylistUiState.PlaylistState.Empty
                else
                    SettingsPlaylistUiState.PlaylistState.Success(lists)

                updateUiState {
                    copy(
                        playlistState = playlistState,
                        isLoading = false,
                    )
                }
            }.launchIn(viewModelScope)
    }

    override fun onAction(uiAction: SettingsPlaylistUiAction) {
        when (uiAction) {
            is SettingsPlaylistUiAction.DeletePlaylist -> deletePlaylist(playlist = uiAction.playlist)
            SettingsPlaylistUiAction.NavigateBack -> viewModelScope.postUiEffect(
                SettingsPlaylistUiEffect.OnNavigateBack
            )

            is SettingsPlaylistUiAction.NavigateToPlaylist ->
                viewModelScope.postUiEffect(SettingsPlaylistUiEffect.OnNavigateToPlaylist(uiAction.id))
        }
    }

    private fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            deletePlaylistUseCase(playlist = playlist)
        }
    }
}

@Immutable
data class SettingsPlaylistUiState(
    val playlistState: PlaylistState = PlaylistState.Empty,
    val isLoading: Boolean = true,
) {
    sealed interface PlaylistState {
        data class Success(val playlists: List<Playlist>) : PlaylistState
        data object Empty : PlaylistState
    }
}

sealed interface SettingsPlaylistUiAction {
    data class DeletePlaylist(val playlist: Playlist) : SettingsPlaylistUiAction
    data class NavigateToPlaylist(val id: String = String.empty) : SettingsPlaylistUiAction
    data object NavigateBack : SettingsPlaylistUiAction
}

sealed interface SettingsPlaylistUiEffect {
    data class OnNavigateToPlaylist(val id: String) : SettingsPlaylistUiEffect
    data object OnNavigateBack : SettingsPlaylistUiEffect
}
