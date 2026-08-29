/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 17:25
 *
 */

package com.mvproject.tinyiptvkmp.features.settings.playlist

import com.mvproject.tinyiptvkmp.core.base.mvi.MviViewModel
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.DeletePlaylistUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.ObservePlaylistsUseCase
import com.mvproject.tinyiptvkmp.features.settings.nav.SettingsNavigator
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.core.component.inject

class SettingsPlaylistViewModel(
    private val observePlaylistsUseCase: ObservePlaylistsUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
) : MviViewModel<SettingsPlaylistState, SettingsPlaylistAction, SettingsPlaylistEffect>() {

    private val navigator: SettingsNavigator by inject()

    override fun createStore() = createStore(
        initialState = SettingsPlaylistState(),
        invokeOnStart = { listenPlaylists() },
    )

    override fun onIntent(intent: SettingsPlaylistAction) {
        when (intent) {
            is SettingsPlaylistAction.DeletePlaylist -> launch { deletePlaylist(playlist = intent.playlist) }
            SettingsPlaylistAction.NavigateBack -> launch { navigator.navigateUp() }
            is SettingsPlaylistAction.NavigateToPlaylist -> launch { navigator.navigateToPlaylist(id = intent.id) }
        }
    }

    private suspend fun listenPlaylists() {
        setState { copy(isLoading = true) }
        observePlaylistsUseCase()
            .distinctUntilChanged()
            .collect { lists ->
                val playlistState = if (lists.isEmpty())
                    SettingsPlaylistState.PlaylistState.Empty
                else
                    SettingsPlaylistState.PlaylistState.Success(lists)

                setState {
                    copy(
                        playlistState = playlistState,
                        isLoading = false,
                    )
                }
            }
    }

    private suspend fun deletePlaylist(playlist: Playlist) {
        deletePlaylistUseCase(playlist = playlist)
    }
}