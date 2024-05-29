/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 17:22
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.groups

import com.mvproject.tinyiptvkmp.data.helpers.PlaylistHelper
import com.mvproject.tinyiptvkmp.data.usecases.GetPlaylistGroupUseCase
import com.mvproject.tinyiptvkmp.ui.screens.groups.action.GroupAction
import com.mvproject.tinyiptvkmp.ui.screens.groups.state.ChannelsGroups
import com.mvproject.tinyiptvkmp.ui.screens.groups.state.GroupState
import com.mvproject.tinyiptvkmp.ui.screens.groups.state.PlaylistNames
import com.mvproject.tinyiptvkmp.ui.screens.groups.state.Playlists
import com.mvproject.tinyiptvkmp.utils.AppConstants.INT_VALUE_1
import com.mvproject.tinyiptvkmp.utils.AppConstants.LONG_NO_VALUE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class GroupViewModel(
    private val playlistHelper: PlaylistHelper,
    private val getPlaylistGroupUseCase: GetPlaylistGroupUseCase,
) : ViewModel() {
    private val _groupState = MutableStateFlow(GroupState())
    val playlistDataState = _groupState.asStateFlow()

    init {
        combine(
            playlistHelper.allPlaylistsFlow(),
            playlistHelper.currentPlaylistId,
        ) { playlists, currentId ->
            _groupState.update { current ->
                current.copy(isLoading = true)
            }
            val currentIndex = playlists.indexOfFirst { it.id == currentId }

            val loadedData =
                if (currentId != LONG_NO_VALUE) {
                    getPlaylistGroupUseCase()
                } else {
                    emptyList()
                }

            _groupState.update { current ->
                current.copy(
                    isPlaylistSelectorVisible = playlists.count() > INT_VALUE_1,
                    playlists = Playlists(items = playlists),
                    playlistNames = PlaylistNames(items = playlists.map { it.playlistTitle }),
                    playlistSelectedIndex = currentIndex,
                    groups = ChannelsGroups(items = loadedData),
                    isLoading = false,
                )
            }
        }.launchIn(viewModelScope)
    }

    fun processAction(action: GroupAction) {
        when (action) {
            is GroupAction.SelectPlaylist -> {
                val playlistIndex = action.id
                val current = playlistDataState.value.playlistSelectedIndex
                if (current != playlistIndex) {
                    viewModelScope.launch {
                        val selected = playlistDataState.value.playlists.items[playlistIndex]
                        playlistHelper.setCurrentPlaylist(playlistId = selected.id)
                    }
                }
            }
        }
    }
}
