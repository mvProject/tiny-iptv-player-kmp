/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.07.24, 12:57
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptvkmp.data.helpers.GroupHelper
import com.mvproject.tinyiptvkmp.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.ui.screens.groups.action.GroupAction
import com.mvproject.tinyiptvkmp.ui.screens.groups.state.ChannelsGroups
import com.mvproject.tinyiptvkmp.ui.screens.groups.state.GroupState
import com.mvproject.tinyiptvkmp.ui.screens.groups.state.GroupUiState
import com.mvproject.tinyiptvkmp.ui.screens.groups.state.PlaylistNames
import com.mvproject.tinyiptvkmp.ui.screens.groups.state.Playlists
import com.mvproject.tinyiptvkmp.utils.AppConstants.INT_VALUE_1
import com.mvproject.tinyiptvkmp.utils.AppConstants.LONG_NO_VALUE
import com.mvproject.tinyiptvkmp.utils.KLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GroupViewModel(
    // private val playlistHelper: PlaylistHelper,
    private val groupHelper: GroupHelper,
    private val preferenceRepository: PreferenceRepository,
    private val playlistsRepository: PlaylistsRepository,
) : ViewModel() {
    private val _groupState = MutableStateFlow(GroupState())
    val groupState = _groupState.asStateFlow()

    private val _groupUiState = MutableStateFlow<GroupUiState>(GroupUiState.Loading)
    val groupUiState = _groupUiState.asStateFlow()

    init {
        combine(
            playlistsRepository.allPlaylistsAsFlow(),
            preferenceRepository.currentPlaylistId,
        ) { playlists, currentId ->

            _groupUiState.update { GroupUiState.Loading }

            val currentIndex = playlists.indexOfFirst { it.id == currentId }

            _groupState.update { current ->
                current.copy(
                    isPlaylistSelectorVisible = playlists.count() > INT_VALUE_1,
                    playlists = Playlists(items = playlists),
                    playlistNames = PlaylistNames(items = playlists.map { it.playlistName }),
                    playlistSelectedIndex = currentIndex,
                    playlistSelectedId = currentId,
                )
            }

            refreshGroups()
        }.launchIn(viewModelScope)
    }

    fun refresh() {
        viewModelScope.launch {
            refreshGroups()
        }
    }

    private suspend fun refreshGroups() {
        val currentId = groupState.value.playlistSelectedId

        if (currentId != LONG_NO_VALUE) {
            val all = groupHelper.getAllGroup()
            val favorites = groupHelper.getFavoriteGroups()
            val groups = groupHelper.getPlaylistGroups()

            _groupState.update { current ->
                current.copy(
                    allGroup = all,
                    favorites = ChannelsGroups(items = favorites),
                    groups = ChannelsGroups(items = groups),
                )
            }
            if (groups.isEmpty()) {
                _groupUiState.update { GroupUiState.Empty }
            } else {
                _groupUiState.update { GroupUiState.Groups }
            }
        } else {
            KLog.w("testing no currentId $currentId")
        }
    }

    fun processAction(action: GroupAction) {
        when (action) {
            is GroupAction.SelectPlaylist -> {
                val playlistIndex = action.id
                val current = groupState.value.playlistSelectedIndex
                if (current != playlistIndex) {
                    viewModelScope.launch {
                        val selected = groupState.value.playlists.items[playlistIndex]
                        preferenceRepository.setCurrentPlaylistId(playlistId = selected.id)
                    }
                }
            }
        }
    }
}
