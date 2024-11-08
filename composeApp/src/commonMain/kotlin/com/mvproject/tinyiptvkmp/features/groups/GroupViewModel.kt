/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.07.24, 12:57
 *
 */

package com.mvproject.tinyiptvkmp.features.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.mvproject.tinyiptvkmp.core.common.AppConstants.INT_VALUE_1
import com.mvproject.tinyiptvkmp.core.common.AppConstants.LONG_NO_VALUE
import com.mvproject.tinyiptvkmp.core.common.mvi.MVI
import com.mvproject.tinyiptvkmp.core.common.mvi.mvi
import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.core.domain.enums.GroupType
import com.mvproject.tinyiptvkmp.core.domain.model.Playlist
import com.mvproject.tinyiptvkmp.core.domain.usecase.CleanProgramsUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.GetPlaylistGroupUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.RefreshEpgChannelsUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.RefreshEpgProgramsUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.SavePlaylistContentUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.SelectPlaylistUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.UpdateChannelsEpgInfoUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.UpdateRemotePlaylistChannelsUseCase
import com.mvproject.tinyiptvkmp.features.groups.GroupContract.UiAction
import com.mvproject.tinyiptvkmp.features.groups.GroupContract.UiEffect
import com.mvproject.tinyiptvkmp.features.groups.GroupContract.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
) : ViewModel(),
    MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    init {
        playlistsRepository
            .allPlaylistsAsFlow()
            .flowOn(Dispatchers.IO)
            .onEach { playlists ->
                updateUiState {
                    copy(
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
                Logger.w("testing isChannelsEpgInfoUpdateRequired isRequired=$isRequired")
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

    override fun onAction(uiAction: UiAction) {
        when (uiAction) {
            UiAction.RefreshPlaylist -> refresh()
            is UiAction.SelectPlaylist -> {
                val selected = uiAction.playlist
                if (!selected.isSelected) {
                    viewModelScope.launch {
                        selectPlaylistUseCase(playlist = selected)
                    }
                }
            }

            is UiAction.NavigateToGroup -> {
                viewModelScope.postUiEffect(
                    UiEffect.NavigateToGroup(
                        title = uiAction.title,
                        group = uiAction.group
                    )
                )
            }

            UiAction.NavigateToSettings -> {
                viewModelScope.postUiEffect(UiEffect.NavigateToSettings)
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            refreshGroups()
        }
    }

    private suspend fun refreshGroups() {
        updateUiState { copy(isLoading = true) }

        val channelGroups = getPlaylistGroupUseCase()

        val groupState = if (channelGroups.none { it.groupType == GroupType.SPECIFIED }) {
            GroupState.Empty
        } else {
            GroupState.Success(channelGroups)
        }

        updateUiState {
            copy(
                groupState = groupState,
                isLoading = false
            )
        }
    }
}