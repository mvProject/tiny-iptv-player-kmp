/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.07.24, 12:57
 *
 */

package com.mvproject.tinyiptvkmp.features.groups

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.mvproject.tinyiptvkmp.core.common.AppConstants.INT_VALUE_1
import com.mvproject.tinyiptvkmp.core.common.mvi.MviCore
import com.mvproject.tinyiptvkmp.core.common.mvi.mviCore
import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.core.domain.enums.GroupType
import com.mvproject.tinyiptvkmp.core.domain.model.ChannelsGroup
import com.mvproject.tinyiptvkmp.core.domain.model.Playlist
import com.mvproject.tinyiptvkmp.core.domain.usecase.CleanProgramsUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.GetPlaylistGroupUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.RefreshEpgChannelsUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.RefreshEpgProgramsUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.SavePlaylistContentUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.SelectPlaylistUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.UpdateChannelsEpgInfoUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.UpdateRemotePlaylistChannelsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
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
) : ViewModel(), MviCore<GroupUiState, GroupUiAction, GroupUiEffect> by mviCore(GroupUiState()) {

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

        combine(
            preferenceRepository.isChannelsEpgInfoUpdateRequired(),
            preferenceRepository.idForPlaylistContentLoad()
        ) { isRequired, id ->
            Logger.w("testing isChannelsEpgInfoUpdateRequired isRequired=$isRequired")
            if (isRequired) {
                updateChannelsEpgInfoUseCase()
            }
            Logger.w("testing idForPlaylistContentLoad id=$id")
            if (id.isNotBlank()) {
                savePlaylistContentUseCase(playlistId = id)
            }
        }.launchIn(viewModelScope)

        viewModelScope.launch(Dispatchers.IO) {
            updateRemotePlaylistChannelsUseCase()

            refreshEpgChannelsUseCase()

            cleanProgramsUseCase()

            refreshEpgProgramsUseCase()
        }
    }

    override fun onAction(uiAction: GroupUiAction) {
        when (uiAction) {
            GroupUiAction.RefreshPlaylist -> refresh()
            is GroupUiAction.SelectPlaylist -> {
                val selected = uiAction.playlist
                if (!selected.isSelected) {
                    viewModelScope.launch {
                        selectPlaylistUseCase(playlist = selected)
                    }
                }
            }

            is GroupUiAction.NavigateToGroup -> {
                viewModelScope.postUiEffect(
                    GroupUiEffect.OnNavigateToGroup(
                        title = uiAction.title,
                        group = uiAction.group
                    )
                )
            }

            GroupUiAction.NavigateToSettings -> {
                viewModelScope.postUiEffect(GroupUiEffect.OnNavigateToSettings)
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
            GroupUiState.GroupState.Empty
        } else {
            GroupUiState.GroupState.Success(channelGroups)
        }

        updateUiState {
            copy(
                groupState = groupState,
                isLoading = false
            )
        }
    }
}

@Immutable
data class GroupUiState(
    val groupState: GroupState = GroupState.Empty,
    val playlists: List<Playlist> = emptyList(),
    val selectedPlaylist: Playlist = Playlist(),
    val isPlaylistSelectorVisible: Boolean = false,
    val isLoading: Boolean = false,
) {
    sealed interface GroupState {
        data class Success(val groups: List<ChannelsGroup>) : GroupState
        data object Empty : GroupState
    }
}

sealed interface GroupUiAction {
    data class SelectPlaylist(val playlist: Playlist) : GroupUiAction
    data class NavigateToGroup(val title: String, val group: String) : GroupUiAction
    data object NavigateToSettings : GroupUiAction
    data object RefreshPlaylist : GroupUiAction
}

sealed interface GroupUiEffect {
    data object OnNavigateToSettings : GroupUiEffect
    data class OnNavigateToGroup(val title: String, val group: String) : GroupUiEffect
}