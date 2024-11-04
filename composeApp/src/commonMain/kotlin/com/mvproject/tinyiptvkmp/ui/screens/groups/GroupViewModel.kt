/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.07.24, 12:57
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptvkmp.data.enums.GroupType
import com.mvproject.tinyiptvkmp.data.model.playlist.Playlist
import com.mvproject.tinyiptvkmp.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.data.usecases.CleanProgramsUseCase
import com.mvproject.tinyiptvkmp.data.usecases.GetPlaylistGroupUseCase
import com.mvproject.tinyiptvkmp.data.usecases.RefreshEpgChannelsUseCase
import com.mvproject.tinyiptvkmp.data.usecases.RefreshEpgProgramsUseCase
import com.mvproject.tinyiptvkmp.data.usecases.SavePlaylistContentUseCase
import com.mvproject.tinyiptvkmp.data.usecases.SelectPlaylistUseCase
import com.mvproject.tinyiptvkmp.data.usecases.UpdateChannelsEpgInfoUseCase
import com.mvproject.tinyiptvkmp.data.usecases.UpdateRemotePlaylistChannelsUseCase
import com.mvproject.tinyiptvkmp.ui.screens.groups.action.GroupAction
import com.mvproject.tinyiptvkmp.ui.screens.groups.state.GroupState
import com.mvproject.tinyiptvkmp.ui.screens.groups.state.GroupUiState
import com.mvproject.tinyiptvkmp.utils.AppConstants.INT_VALUE_1
import com.mvproject.tinyiptvkmp.utils.AppConstants.LONG_NO_VALUE
import com.mvproject.tinyiptvkmp.utils.KLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GroupViewModel(
    private val preferenceRepository: PreferenceRepository,
    private val playlistsRepository: PlaylistsRepository,
    private val selectPlaylistUseCase: SelectPlaylistUseCase,
    private val getPlaylistGroupUseCase: GetPlaylistGroupUseCase,
    private val refreshEpgChannelsUseCase: RefreshEpgChannelsUseCase,
    private val refreshEpgProgramsUseCase: RefreshEpgProgramsUseCase,
    private val updateRemotePlaylistChannelsUseCase: UpdateRemotePlaylistChannelsUseCase,
    private val savePlaylistContentUseCase: SavePlaylistContentUseCase,
    private val updateChannelsEpgInfoUseCase: UpdateChannelsEpgInfoUseCase,
    private val cleanProgramsUseCase: CleanProgramsUseCase,
) : ViewModel() {
    private val _groupState = MutableStateFlow(GroupState())
    val groupState = _groupState.asStateFlow()

    private val _groupUiState = MutableStateFlow<GroupUiState>(GroupUiState.Loading)
    val groupUiState = _groupUiState.asStateFlow()

    init {
        playlistsRepository
            .allPlaylistsAsFlow()
            .flowOn(Dispatchers.IO)
            .onEach { playlists ->

                _groupState.update { current ->
                    current.copy(
                        isPlaylistSelectorVisible = playlists.count() > INT_VALUE_1,
                        playlists = playlists,
                        selectedPlaylist = playlists.firstOrNull { it.isSelected } ?: Playlist(),
                    )
                }

                refreshGroups()
            }.launchIn(viewModelScope)

        preferenceRepository
            .isChannelsEpgInfoUpdateRequired()
            .flowOn(Dispatchers.IO)
            .onEach { isRequired ->
                KLog.w("testing isChannelsEpgInfoUpdateRequired isRequired=$isRequired")
                if (isRequired) {
                    updateChannelsEpgInfoUseCase()
                }
            }.launchIn(viewModelScope)

        preferenceRepository
            .idForPlaylistContentLoad()
            .flowOn(Dispatchers.IO)
            .onEach { id ->
                if (id != LONG_NO_VALUE) {
                    savePlaylistContentUseCase(playlistId = id)
                }
            }.launchIn(viewModelScope)

        viewModelScope.launch(Dispatchers.IO) {
            refreshEpgProgramsUseCase()
        }

        viewModelScope.launch(Dispatchers.IO) {
            refreshEpgChannelsUseCase()
        }

        viewModelScope.launch(Dispatchers.IO) {
            updateRemotePlaylistChannelsUseCase()
        }

        viewModelScope.launch(Dispatchers.IO) {
            cleanProgramsUseCase()
        }
    }

    fun processAction(action: GroupAction) {
        when (action) {
            is GroupAction.SelectPlaylist -> {
                val selected = action.playlist
                if (!selected.isSelected) {
                    viewModelScope.launch {
                        selectPlaylistUseCase(playlist = selected)
                    }
                }
            }

            GroupAction.RefreshPlaylist -> refresh()
        }
    }

    private fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            refreshGroups()
        }
    }

    private suspend fun refreshGroups() {
        val channelGroups = getPlaylistGroupUseCase()

        _groupState.update { current ->
            current.copy(channelGroups = channelGroups)
        }

        if (channelGroups.none { it.groupType == GroupType.SPECIFIED }) {
            _groupUiState.update {
                GroupUiState.Empty
            }
        } else {
            _groupUiState.update {
                GroupUiState.Groups
            }
        }
    }
}
