/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 27.10.23, 21:28
 *
 */

package com.mvproject.tinyiptv.ui.screens.settings.playlist

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
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
) : ScreenModel {

    private val _playlistDataState = MutableStateFlow(SettingsPlaylistState())
    val playlistDataState = _playlistDataState.asStateFlow()

    init {
        coroutineScope.launch(Dispatchers.IO) {
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
                coroutineScope.launch {
                    deletePlaylistUseCase(playlist = action.playlist)
                }
            }
        }
    }
}