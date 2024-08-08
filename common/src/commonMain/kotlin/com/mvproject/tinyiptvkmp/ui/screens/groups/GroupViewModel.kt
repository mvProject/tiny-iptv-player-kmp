/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.07.24, 12:57
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.groups

import com.mvproject.tinyiptvkmp.data.helpers.GroupHelper
import com.mvproject.tinyiptvkmp.data.helpers.PlaylistHelper
import com.mvproject.tinyiptvkmp.ui.screens.groups.action.GroupAction
import com.mvproject.tinyiptvkmp.ui.screens.groups.state.ChannelsGroups
import com.mvproject.tinyiptvkmp.ui.screens.groups.state.GroupState
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
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class GroupViewModel(
    private val playlistHelper: PlaylistHelper,
    private val groupHelper: GroupHelper,
) : ViewModel() {
    private val _groupState = MutableStateFlow(GroupState())
    val groupState = _groupState.asStateFlow()

    init {
        combine(
            playlistHelper.allPlaylistsFlow(),
            playlistHelper.currentPlaylistId,
        ) { playlists, currentId ->
            _groupState.update { current ->
                current.copy(isLoading = true)
            }
            val currentIndex = playlists.indexOfFirst { it.id == currentId }

            KLog.w("testing init")

            _groupState.update { current ->
                current.copy(
                    isPlaylistSelectorVisible = playlists.count() > INT_VALUE_1,
                    playlists = Playlists(items = playlists),
                    playlistNames = PlaylistNames(items = playlists.map { it.playlistTitle }),
                    playlistSelectedIndex = currentIndex,
                    playlistSelectedId = currentId,
                    isLoading = false,
                )
            }

            refreshGroups(currentId = currentId)
        }.launchIn(viewModelScope)
    }

    fun refresh() {
        KLog.w("testing refresh")
        viewModelScope.launch {
            val currentId = groupState.value.playlistSelectedId
            refreshGroups(currentId = currentId)
        }
    }

    private suspend fun refreshGroups(currentId: Long) {
        if (currentId != LONG_NO_VALUE) {
            _groupState.update { current ->
                val all = groupHelper.getAllGroup()
                val favorites = groupHelper.getFavoriteGroups()
                val groups = groupHelper.getPlaylistGroups()

                current.copy(
                    allGroup = all,
                    favorites = ChannelsGroups(items = favorites),
                    groups = ChannelsGroups(items = groups),
                )
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
                        playlistHelper.setCurrentPlaylist(playlistId = selected.id)
                    }
                }
            }
        }
    }
}
